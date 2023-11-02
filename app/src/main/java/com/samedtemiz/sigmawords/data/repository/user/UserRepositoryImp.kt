package com.samedtemiz.sigmawords.data.repository.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.samedtemiz.sigmawords.data.model.Quiz
import com.samedtemiz.sigmawords.data.model.User
import com.samedtemiz.sigmawords.util.Constant.CURRENT_DATE
import com.samedtemiz.sigmawords.util.UiState

private const val TAG = "UserRepository"
class UserRepositoryImp(
    private val database: FirebaseFirestore
) : UserRepository {

    // Quiz Operations
    override fun getQuiz(userId: String, quiz: MutableLiveData<UiState<Quiz>>) {
        val quizzesCollection =
            database.collection("UserDatabase").document(userId).collection("Quizzes")

        quizzesCollection.whereEqualTo("date", CURRENT_DATE)
            .get()
            .addOnSuccessListener { documents ->
                if(!documents.isEmpty){
                    for (document in documents) {
                        val quizData = document.toObject<Quiz>()
                        quiz.postValue(UiState.Success(quizData))
                    }
                    Log.w(TAG, "Quiz verisi alındı.")
                }else{
                    quiz.postValue(UiState.Failure("Quiz bulunamadı."))
                    Log.w(TAG, "Quiz verisi alınamadı.")
                }

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Failure: ", exception)
            }
    }
    override fun addQuiz(userId: String, quiz: Quiz) {
        val quizzesCollection = database.collection("UserDatabase").document(userId).collection("Quizzes")

        quizzesCollection.add(quiz)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Quiz eklendi: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Quiz eklenirken hata oluştu", e)
            }
    }

    override fun updateQuiz(userId: String, isSolved: Boolean) {
        val quizzesCollection =
            database.collection("UserDatabase").document(userId).collection("Quizzes")

        val currentDate = CURRENT_DATE

        quizzesCollection
            .whereEqualTo("date", currentDate)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val docRef = quizzesCollection.document(document.id)
                    docRef.update("solved", isSolved)
                        .addOnSuccessListener {
                            Log.d(TAG, "Quiz güncellendi: $isSolved")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Quiz güncellenirken hata oluştu.", e)
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Quiz verisi alınamadı: ", exception)
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