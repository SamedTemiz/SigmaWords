package com.samedtemiz.sigmawords.data.repository.user

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.samedtemiz.sigmawords.data.model.User

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
        if(usersRef.get().isSuccessful){
            return true
        }

        return false
    }

}