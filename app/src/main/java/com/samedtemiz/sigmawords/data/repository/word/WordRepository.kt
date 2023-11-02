package com.samedtemiz.sigmawords.data.repository.word

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.samedtemiz.sigmawords.data.model.Word
import com.samedtemiz.sigmawords.util.UiState

interface WordRepository {

    fun getWords(result: MutableLiveData<List<Word>>, wordsListName: String)
    fun getSigmaWords(result: MutableLiveData<List<Word>>, wordsListName: String)
    fun getWordWithId(id: String, result: MutableLiveData<Word>, wordsListName: String)
}