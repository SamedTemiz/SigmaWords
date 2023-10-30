package com.samedtemiz.sigmawords.data.repository.word

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.type.DateTime
import com.samedtemiz.sigmawords.data.model.Word
import com.samedtemiz.sigmawords.data.repository.word.WordRepository
import com.samedtemiz.sigmawords.util.UiState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WordRepositoryImp(
    private val database: FirebaseFirestore
) : WordRepository {
    private val TAG = "WordRepository"

    override fun getAllWords(result: MutableLiveData<List<Word>>, wordsListName: String) {
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
//                result.invoke(
//                    UiState.Success(words)
//                )
                result.postValue(words)
            }
            .addOnFailureListener {
//                result.invoke(
//                    UiState.Failure(
//                        it.localizedMessage
//                    )
//                )
                result.postValue(null)
            }
    }

    override fun getSigmaWords(result: MutableLiveData<List<Word>>, wordsListName: String) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        database
            .collection("AppDatabase")
            .document("Words")
            .collection(wordsListName)
            .whereEqualTo("sigmaDate", LocalDateTime.now().format(formatter))
            .get()
            .addOnSuccessListener { documents ->
                val words = arrayListOf<Word>()
                for (document in documents) {
                    val word = document.toObject<Word>()
                    words.add(word)
                }

                result.postValue(words)
            }
            .addOnFailureListener {
                result.postValue(null)
                Log.d(TAG, "${it.message}")
            }
    }

}