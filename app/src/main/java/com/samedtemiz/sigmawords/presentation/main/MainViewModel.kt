package com.samedtemiz.sigmawords.presentation.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.samedtemiz.sigmawords.data.repository.word.WordRepository
import com.samedtemiz.sigmawords.data.model.Word
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val wordRepository: WordRepository,
    private val firebaseDb: FirebaseFirestore,
) : ViewModel() {

    val wordsList: MutableLiveData<List<Word>> = MutableLiveData()

    init {
        viewModelScope.launch {
            try {
                wordRepository.getAllWordsFromRoom().collect { words ->
                    // Flow'dan gelen veriyi kullan
                    wordsList.value = words
                }
            } catch (e: Exception) {
                // Hata yönetimi
                Log.e("MainViewModel", "Veri çekme hatası: ${e.message}")
            }
        }
    }

    fun syncWordsFromApi() = viewModelScope.launch {
        Log.d("Retrofit", "Words listesi dolduruluyor.")

        wordRepository.getAllWordsFromApi().let { response ->
            try {
                if (response.isSuccessful) {
                    val words = response.body()
                    if (words != null) {
                        wordRepository.insertAllWords(words)
                    } else {
                        Log.e("Retrofit", "Error: Response body is null")
                    }
                } else {
                    // Hata durumunu
                    Log.e("Retrofit", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                // Network hatası veya diğer hataları işle
                Log.e("Retrofit", "Exception: ${e.message}")
            }
        }
    }


//    fun syncWordsFromFirebase() {
//        Log.d("ROOM", "Words listesi dolduruluyor.")
//        viewModelScope.launch {
//            try {
//                val documents = withContext(Dispatchers.IO) {
//                    firebaseDb
//                        .collection("AppDatabase")
//                        .document("Words")
//                        .collection("AllWords")
//                        .get()
//                        .await()
//                }
//
//                val words = mutableListOf<Word>()
//                for (document in documents) {
//                    val word = document.toObject<Word>()
//                    words.add(word)
//                }
//
//                // Firebase'den çekilen veriyi yerel veritabanına ekle
//                wordRepository.insertAllWords(words = words)
//
//                // LiveData'yı güncelle
//                // Not: Bu işlem otomatik olarak yapılabilir, LiveData verisi değiştiğinde UI otomatik olarak güncellenir.
//            } catch (e: Exception) {
//                // Hata yönetimi
//                Log.d("ROOM", e.message.toString())
//            }
//        }
//    }
}


