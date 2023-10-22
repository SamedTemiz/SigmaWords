package com.samedtemiz.sigmawords.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.samedtemiz.sigmawords.data.model.Word
import com.samedtemiz.sigmawords.util.FireStoreValues
import com.samedtemiz.sigmawords.util.UiState

class WordRepositoryImp(
    private val database: FirebaseFirestore
) : WordRepository {
    override fun getWords(result: (UiState<List<Word>>) -> Unit) {
        database
            .collection("AppDatabase")
            .document("Words")
            .collection("A1")
            .get()
            .addOnSuccessListener { documents ->
                val words = arrayListOf<Word>()
                for (document in documents) {
                    val word = document.toObject<Word>()
                    words.add(word)
                }

                // Geldiğimiz yere result yolluyoruz
                result.invoke(
                    UiState.Success(words)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }
}