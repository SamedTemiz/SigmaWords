package com.samedtemiz.sigmawords.data.repository.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.samedtemiz.sigmawords.data.model.Quiz
import com.samedtemiz.sigmawords.data.model.Result
import com.samedtemiz.sigmawords.data.model.User
import com.samedtemiz.sigmawords.util.Constant.CURRENT_DATE
import com.samedtemiz.sigmawords.util.UiState

private const val TAG = "UserRepository"

class UserRepositoryImp(
    private val database: FirebaseFirestore
) : UserRepository {

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
                        quiz.postValue(UiState.Success(quizData))
                    }
                    Log.w(TAG, "Quiz verisi alındı.")
                } else {
                    quiz.postValue(UiState.Failure("Quiz bulunamadı."))
                    Log.w(TAG, "Quiz verisi alınamadı.")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Failure: ", exception)
            }
    }
    override fun addQuiz(userId: String, quiz: Quiz) {
        val quizzesCollection =
            database.collection("UserDatabase").document(userId).collection("Quizzes")

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
    override fun updateQuiz(userId: String, quiz: Quiz) {
        val quizzesCollection =
            database.collection("UserDatabase").document(userId).collection("Quizzes")

        val quizId = quiz.quizId // Quiz'in kimlik bilgisini al

        quizzesCollection.document(quizId!!).set(quiz) // Quiz'i belgeye yaz

        Log.d(TAG, "Quiz güncellendi: $quizId")
    }


    // Quiz Result Operations - Progress
    override fun addResult(userId: String, result: Result) {
        val quizzesCollection =
            database.collection("UserDatabase").document(userId).collection("Results")

        quizzesCollection.add(result)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Result eklendi: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Result eklenirken hata:", e)
            }
    }
    override fun getResult(userId: String, quizId: String, result: MutableLiveData<UiState<Result>>) {
        val quizzesCollection =
            database.collection("UserDatabase").document(userId).collection("Results")

        quizzesCollection.whereEqualTo("quizId", quizId)
            .get()
            .addOnSuccessListener { documents ->
                if(!documents.isEmpty){
                    for(document in documents){
                        val resultData = document.toObject<Result>()
                        result.postValue(UiState.Success(resultData))
                    }
                    Log.d(TAG, "Result verisi alındı.")
                }else {
                    result.postValue(UiState.Failure("Result bulunamadı."))
                    Log.w(TAG, "Result verisi alınamadı.")
                }
            }
            .addOnFailureListener { e ->
                result.postValue(UiState.Failure("Error getting result: $e"))
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
                    Log.d(TAG, "Document already exists")
                } else {
                    database.collection("UserDatabase")
                        .document(user.userId)
                        .set(userData)
                        .addOnSuccessListener {
                            Log.d(TAG, "DocumentSnapshot successfully written!")
                        }
                        .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
                }
            } else {
                Log.d(TAG, "get failed with ", task.exception)
            }
        }
    }
}