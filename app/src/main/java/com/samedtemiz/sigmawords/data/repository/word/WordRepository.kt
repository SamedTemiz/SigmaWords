package com.samedtemiz.sigmawords.data.repository.word

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.samedtemiz.sigmawords.data.model.Word
import com.samedtemiz.sigmawords.util.UiState

interface WordRepository {

    fun getAllWords(result: MutableLiveData<List<Word>>, wordsListName: String)
    fun getSigmaWords(result: MutableLiveData<List<Word>>, wordsListName: String)

}