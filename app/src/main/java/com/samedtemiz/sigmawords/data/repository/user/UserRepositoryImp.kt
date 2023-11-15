package com.samedtemiz.sigmawords.data.repository.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.functions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.samedtemiz.sigmawords.data.model.Quiz
import com.samedtemiz.sigmawords.data.model.Result
import com.samedtemiz.sigmawords.data.model.User
import com.samedtemiz.sigmawords.data.model.Word
import com.samedtemiz.sigmawords.util.Constant.CURRENT_DATE
import com.samedtemiz.sigmawords.util.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private const val TAG = "UserRepository"

class UserRepositoryImp(private val database: FirebaseFirestore) : UserRepository {

    // Quiz Operations - Quizzes
    override fun getQuiz(userId: String, quiz: MutableLiveData<UiState<Quiz>>) {
        val quizzesCollection =
            database.collection("UserDatabase").document(userId).collection("Quizzes")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val documents = quizzesCollection
                    .whereEqualTo("date", CURRENT_DATE)
                    .get()
                    .await()

                if (!documents.isEmpty) {
                    val quizData = documents.documents[0].toObject<Quiz>()
                    quiz.postValue(UiState.Success(quizData!!))
                } else {
                    quiz.postValue(UiState.Failure("Quiz bulunamadı."))
                }
            } catch (e: Exception) {
                Log.w(TAG, "Quiz alınırken hata: ", e)
                quiz.postValue(UiState.Failure("Quiz alınırken bir hata oluştu: ${e.localizedMessage}"))
            }
        }
    }
    override fun addQuiz(userId: String, quiz: Quiz) {
        val quizzesCollection =
            database.collection("UserDatabase").document(userId).collection("Quizzes")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val documentReference =  quizzesCollection.document()
                val quizId = documentReference.id
                val quizWithId = quiz.copy(quizId = quizId)

                documentReference.set(quizWithId).await()

                Log.d(TAG, "Quiz eklendi: $quizId")
            } catch (e: Exception) {
                Log.w(TAG, "Quiz eklenirken hata oluştu: ${e.localizedMessage}")
            }
        }
    }
    override fun updateQuiz(userId: String, quiz: Quiz) {
        val quizzesCollection =
            database.collection("UserDatabase").document(userId).collection("Quizzes")

        val quizId = quiz.quizId // Quiz'in kimlik bilgisini al

        CoroutineScope(Dispatchers.IO).launch {
            try {
                quizzesCollection.document(quizId!!)
                    .set(quiz)
                    .await()

                Log.d(TAG, "Quiz güncellendi: $quizId")
            } catch (e: Exception) {
                Log.w(TAG, "Quiz güncelleme hatası: ${e.localizedMessage}")
            }
        }
    }
    override fun checkQuiz(userId: String, dailyQuiz: MutableLiveData<UiState<Boolean>>) {
        val path = database.collection("UserDatabase").document(userId).collection("Quizzes")

        path.whereEqualTo("date", CURRENT_DATE)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val solved = querySnapshot.documents.firstOrNull()?.getBoolean("solved") ?: false
                dailyQuiz.postValue(UiState.Success(solved))
            }
            .addOnFailureListener { exception ->
                // Hata durumunda burada işlem yapabilirsiniz
                dailyQuiz.postValue(UiState.Failure("Quiz kontrol edilirken bir hata oluştu: ${exception.localizedMessage}"))
            }
    }



    // Sigma Operations
    override fun getUserSigmaWords(userId: String, currentDate: String, result: MutableLiveData<List<Word>>) {
        val wordsCollection =
            database.collection("UserDatabase").document(userId).collection("SigmaWords")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val documents = wordsCollection
                    .whereEqualTo("sigmaDate", currentDate)
                    .get()
                    .await()

                if (!documents.isEmpty) {
                    val wordList = documents.toObjects<Word>()
                    result.postValue(wordList)
                } else {
                    result.postValue(emptyList())
                }
            } catch (e: Exception) {
                Log.w(TAG, "Sigma kelimeler alınırken hata: ", e)
            }
        }
    }
    override fun addSigmaWords(userId: String, sigmaWords: List<Word>) {
        val wordsCollection =
            database.collection("UserDatabase").document(userId).collection("SigmaWords")

        CoroutineScope(Dispatchers.IO).launch {
            for (word in sigmaWords) {
                val wordId = word.id ?: ""

                try {
                    wordsCollection.document(wordId).set(word).await()
                } catch (e: Exception) {
                    Log.w(TAG, "Sigma Word eklenirken hata: ${e.localizedMessage}")
                }
            }
        }
    }
    override fun deleteSigmaWords(userId: String, forDeleteSigmaWords: List<Word>) {
        val wordsCollection =
            database.collection("UserDatabase").document(userId).collection("SigmaWords")

        CoroutineScope(Dispatchers.IO).launch {
            for (word in forDeleteSigmaWords) {
                val wordId = word.id ?: ""

                try {
                    wordsCollection.document(wordId).delete().await()
                } catch (e: Exception) {
                    Log.w(TAG, "Sigma Word silinirken hata: ${e.localizedMessage}")
                }
            }
        }
    }



    // Quiz Result Operations - Progress
    override fun addResult(userId: String, result: Result) {
        val quizzesCollection =
            database.collection("UserDatabase").document(userId).collection("Results")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val documentReference = quizzesCollection.add(result).await()
                Log.d(TAG, "Result eklendi: ${documentReference.id}")
            } catch (e: Exception) {
                Log.w(TAG, "Result eklenirken hata: ${e.localizedMessage}")
            }
        }
    }
    override fun getCurrentResult(userId: String, quizId: String, result: MutableLiveData<UiState<Result>>) {
        val quizzesCollection =
            database.collection("UserDatabase").document(userId).collection("Results")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val documents = quizzesCollection
                    .whereEqualTo("quizId", quizId)
                    .get()
                    .await()

                if (!documents.isEmpty) {
                    val resultData = documents.documents[0].toObject<Result>()
                    result.postValue(UiState.Success(resultData!!))
                } else {
                    result.postValue(UiState.Failure("Result bulunamadı."))
                }
            } catch (e: Exception) {
                result.postValue(UiState.Failure("Error getting result: ${e.localizedMessage}"))
            }
        }
    }
    override fun getResultList(userId: String, result: MutableLiveData<UiState<List<Result>>>) {
        val quizzesCollection =
            database.collection("UserDatabase").document(userId).collection("Results")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val documents = quizzesCollection
                    .orderBy("resultDate", Query.Direction.DESCENDING)
                    .get()
                    .await()

                if (!documents.isEmpty) {
                    val resultList = documents.toObjects<Result>()
                    result.postValue(UiState.Success(resultList))
                } else {
                    result.postValue(UiState.Failure("Result listesi oluşturulamadı."))
                }
            } catch (e: Exception) {
                result.postValue(UiState.Failure("Error getting result: ${e.localizedMessage}"))
            }
        }
    }



    // User Database Operations
    override fun createUserDatabase(user: User) {
        val userData = user.copy(registerDate = CURRENT_DATE)
        val userId = user.userId ?: ""

        val userDocumentReference = database.collection("UserDatabase").document(userId)
        CoroutineScope(Dispatchers.IO).launch {
            userDocumentReference
                .set(userData)
                .addOnSuccessListener {
                    Log.d(TAG, "Kullanıcı veritabanı oluşturuldu.")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Veritabanına yazma hatası", e)
                }
        }
    }
    override fun getUserDatabase(userId: String, userData: MutableLiveData<UiState<User>>) {
        val userRef = database.collection("UserDatabase").document(userId)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val documentSnapshot = userRef.get().await()

                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject<User>()
                    userData.postValue(UiState.Success(user!!))
                } else {
                    userData.postValue(UiState.Failure("User bilgisi alınamadı"))
                }
            } catch (exception: Exception) {
                // Hata durumunda
                userData.postValue(UiState.Failure(exception.localizedMessage))
            }
        }
    }
    override fun deleteUserDatabase(userId: String): Boolean {
        val userRef = database.collection("UserDatabase").document(userId)

        // Belirli koleksiyonları boşaltmak istediğimizde
        val collectionsToDelete = listOf("Quizzes", "Results", "SigmaWords")

        // Her koleksiyon için işlemi gerçekleştir
        collectionsToDelete.forEach { collectionName ->
            val collectionRef = userRef.collection(collectionName)
            deleteCollection(collectionRef)
        }

        return true
    }
    private fun deleteCollection(collection: CollectionReference) {
        val batchSize = 500 // Toplu işlem boyutu, isteğe bağlı olarak ayarlanabilir
        val query = collection.limit(batchSize.toLong())

        query.get().addOnSuccessListener { documents ->
            if (documents.size() == 0) {
                // Koleksiyon boş, işlemi sonlandır
                return@addOnSuccessListener
            }

            // Dokümanları sil
            for (document in documents) {
                document.reference.delete()
            }

            // Özyinelemeli olarak devam et
            deleteCollection(collection)
        }
    }
    override fun deleteUser(userId: String): Boolean {
        var result = false
        try {
            // Belirtilen kullanıcı belgesini sil
            database.collection("UserDatabase").document(userId)
                .delete()
                .addOnSuccessListener {
                    result = true
                }
                .addOnFailureListener { e ->
                    result = false
                }
            return result
        } catch (e: Exception) {
            // Hata yönetimi
            return false
        }
    }

}