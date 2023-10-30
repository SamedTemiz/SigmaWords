package com.samedtemiz.sigmawords.data.repository.user

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.snapshots
import com.samedtemiz.sigmawords.data.model.User
import com.samedtemiz.sigmawords.data.model.Word
import com.samedtemiz.sigmawords.util.Constant.DATE_FORMATTER
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UserRepositoryImp(
    private val database: FirebaseFirestore
) : UserRepository {
    override fun createUserDatabase(user: User) {
        val userData = hashMapOf(
            "username" to user.username,
            "profilePictureUrl" to user.profilePictureUrl,
        )

        if (!checkUserDatabaseExist(user.userId)) {
            database
                .collection("UserDatabase")
                .document(user.userId)
                .set(userData)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully written!")
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }

    }

    override fun checkUserDatabaseExist(id: String): Boolean {
        val usersRef = database.collection("UserDatabase").document(id)
        if (usersRef.get().isSuccessful) {
            return true
        }

        return false
    }

    override fun updateProgress(userId: String, sigmaWords: List<Word>) {
        // TO - DO
    }

    override fun dailyQuizStatus(
        userId: String,
        currentDate: String,
        isSolved: MutableLiveData<Boolean>
    ) {
        database
            .collection("UserDatabase")
            .document(userId)
            .collection("Progress")
            .document("DailyQuiz")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val isQuizSolved = document.getBoolean("isSolved") ?: false
                    val solvedDate = document.getString("solvedDate") ?: ""

                    // Eğer kaydedilen tarih ve geçerli tarih farklıysa, isSolved değerini false olarak ayarla
                    if (solvedDate != currentDate) {
                        isSolved.value = false
                    } else {
                        isSolved.value = isQuizSolved
                    }
                }
            }
    }




    override fun updateQuizStatus(userId: String, isSolved: Boolean, solvedDate: String) {
        val docRef = database
            .collection("UserDatabase")
            .document(userId)
            .collection("Progress")
            .document("DailyQuiz")

        val updateData = hashMapOf(
            "isSolved" to isSolved,
            "solvedDate" to solvedDate
        )

        docRef.set(updateData, SetOptions.merge())
            .addOnSuccessListener {
                Log.d("Firestore", "isSolved değeri başarıyla güncellendi.")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "isSolved değeri güncellenirken hata oluştu.", e)
            }
    }

}