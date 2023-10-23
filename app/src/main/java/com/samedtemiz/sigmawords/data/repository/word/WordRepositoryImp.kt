package com.samedtemiz.sigmawords.data.repository.word

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.samedtemiz.sigmawords.data.model.Word
import com.samedtemiz.sigmawords.data.repository.word.WordRepository
import com.samedtemiz.sigmawords.util.UiState

class WordRepositoryImp(
    private val database: FirebaseFirestore
) : WordRepository {
    override fun getAllWords(result: (UiState<List<Word>>) -> Unit, wordsListName: String) {
        database
            .collection("AppDatabase")
            .document("Words")
            .collection(wordsListName)
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