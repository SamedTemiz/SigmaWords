package com.samedtemiz.sigmawords.data.repository.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.samedtemiz.sigmawords.data.model.Quiz
import com.samedtemiz.sigmawords.data.model.Result
import com.samedtemiz.sigmawords.data.model.User
import com.samedtemiz.sigmawords.data.model.Word
import com.samedtemiz.sigmawords.util.Constant.CURRENT_DATE
import com.samedtemiz.sigmawords.util.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "UserRepository"

class UserRepositoryImp(private val database: FirebaseFirestore) : UserRepository {

    // Quiz Operations - Quizzes
    override fun getQuiz(userId: String, quiz: MutableLiveData<UiState<Quiz>>) {
        val quizzesCollection =
            database.collection("UserDatabase").document(userId).collection("Quizzes")

        quizzesCollection.whereEqualTo("date", CURRENT_DATE)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents) {
                        val quizData = document.toObject<Quiz>()
                        CoroutineScope(Dispatchers.IO).launch {
                            quiz.postValue(UiState.Success(quizData))
                        }

                    }
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        quiz.postValue(UiState.Failure("Quiz bulunamadı."))
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Quiz alınırken hata: ", exception)
            }
    }

    override fun addQuiz(userId: String, quiz: Quiz) {
        val quizzesCollection =
            database.collection("UserDatabase").document(userId).collection("Quizzes")

        CoroutineScope(Dispatchers.IO).launch {
            quizzesCollection.add(quiz)
                .addOnSuccessListener { documentReference ->
                    val quizId = documentReference.id
                    val updatedQuiz = quiz.copy(quizId = quizId) // quizId değerini güncelleyin
                    quizzesCollection.document(quizId)
                        .set(updatedQuiz) // Güncellenmiş quiz'i belgeye yazın
                    Log.d(TAG, "Quiz eklendi: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Quiz eklenirken hata oluştu", e)
                }
        }
    }

    override fun updateQuiz(userId: String, quiz: Quiz) {
        Log.d(TAG, "4 -> " + quiz.quizId.toString())
        val quizzesCollection =
            database.collection("UserDatabase").document(userId).collection("Quizzes")

        val quizId = quiz.quizId // Quiz'in kimlik bilgisini al
        CoroutineScope(Dispatchers.IO).launch {
            quizzesCollection.document(quizId!!)
                .set(quiz)
                .addOnSuccessListener {
                    Log.d(TAG, "Quiz güncellendi: $quizId")
                }
                .addOnFailureListener {
                    Log.d(TAG, "Quiz güncelleme hatası: " + it.localizedMessage)
                }
        }
    }


    // Sigma Operations
    override fun getUserSigmaWords(
        userId: String,
        currentDate: String,
        result: MutableLiveData<List<Word>>
    ) {
        val wordsCollection =
            database.collection("UserDatabase").document(userId).collection("SigmaWords")

        wordsCollection.whereEqualTo("sigmaDate", currentDate)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val wordList = mutableListOf<Word>()
                    for (document in documents) {
                        val word = document.toObject<Word>()
                        wordList.add(word)
                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        result.postValue(wordList)
                    }
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        result.postValue(null)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Sigma kelimeler alınırken hata: ", exception)
            }
    }

    override fun addSigmaWords(userId: String, sigmaWords: List<Word>) {
        TODO("Not yet implemented")
    }


    // Quiz Result Operations - Progress
    override fun addResult(userId: String, result: Result) {
        val quizzesCollection =
            database.collection("UserDatabase").document(userId).collection("Results")
        CoroutineScope(Dispatchers.IO).launch {
            quizzesCollection.add(result)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "Result eklendi: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Result eklenirken hata:", e)
                }
        }
    }

    override fun getCurrentResult(
        userId: String,
        quizId: String,
        result: MutableLiveData<UiState<Result>>
    ) {
        val quizzesCollection =
            database.collection("UserDatabase").document(userId).collection("Results")

        quizzesCollection.whereEqualTo("quizId", quizId)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents) {
                        val resultData = document.toObject<Result>()
                        CoroutineScope(Dispatchers.IO).launch {
                            result.postValue(UiState.Success(resultData))
                        }
                    }
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        result.postValue(UiState.Failure("Result bulunamadı."))
                    }
                }
            }
            .addOnFailureListener { e ->
                CoroutineScope(Dispatchers.IO).launch {
                    result.postValue(UiState.Failure("Error getting result: $e"))
                }
            }
    }

    override fun getResultList(userId: String, result: MutableLiveData<UiState<List<Result>>>) {
        val quizzesCollection =
            database.collection("UserDatabase").document(userId).collection("Results")

        quizzesCollection.orderBy("resultDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val resultList = mutableListOf<Result>() // Result tutan list

                    for (document in documents) {
                        val resultData = document.toObject<Result>()
                        resultList.add(resultData)
                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        result.postValue(UiState.Success(resultList))
                    }

                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        result.postValue(UiState.Failure("Result listesi oluşturuLAMADI."))
                    }
                }
            }
            .addOnFailureListener { e ->
                CoroutineScope(Dispatchers.IO).launch {
                    result.postValue(UiState.Failure("Error getting result: $e"))
                }
            }
    }


    // User Database Operations
    override fun createUserDatabase(user: User) {
        val userData = hashMapOf(
            "username" to user.username,
            "profilePictureUrl" to user.profilePictureUrl,
        )

        val usersRef = database.collection("UserDatabase").document(user.userId)

        usersRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null && document.exists()) {

                } else {
                    database.collection("UserDatabase")
                        .document(user.userId)
                        .set(userData)
                        .addOnSuccessListener {
                            Log.d(TAG, "Kullanıcı veri tabanı oluşturuldu.")
                        }
                        .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
                }
            } else {
                Log.d(TAG, "get failed with ", task.exception)
            }
        }
    }
}