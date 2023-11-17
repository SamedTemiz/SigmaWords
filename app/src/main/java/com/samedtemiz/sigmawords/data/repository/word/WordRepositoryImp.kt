package com.samedtemiz.sigmawords.data.repository.word

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.samedtemiz.sigmawords.data.model.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


private const val TAG = "Word Repository"

class WordRepositoryImp(
    private val database: FirebaseFirestore
) : WordRepository {


    override fun getWords(result: MutableLiveData<List<Word>>, wordsListName: String) {
        database
            .collection("AppDatabase")
            .document("Words")
            .collection(wordsListName)
            .get()
            .addOnSuccessListener { documents ->
                val words = mutableListOf<Word>()
                for (document in documents) {
                    val word = document.toObject<Word>()
                    words.add(word)
                }

                CoroutineScope(Dispatchers.IO).launch {
                    result.postValue(words)
                }
            }
            .addOnFailureListener {
                CoroutineScope(Dispatchers.IO).launch {
                    result.postValue(null)
                }
            }
    }

    override fun getWordWithId(id: String, result: MutableLiveData<Word>, wordsListName: String) {
        database
            .collection("AppDatabase")
            .document("Words")
            .collection(wordsListName)
            .whereEqualTo("id", id)
            .get()
            .addOnSuccessListener {
                val word = it.documents.get(0).toObject<Word>()
                result.postValue(word)
            }
            .addOnFailureListener {
                result.postValue(null)
                Log.d(TAG, "${it.message}")
            }
    }

}