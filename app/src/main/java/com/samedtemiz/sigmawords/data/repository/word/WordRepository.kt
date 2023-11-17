package com.samedtemiz.sigmawords.data.repository.word

import androidx.lifecycle.MutableLiveData
import com.samedtemiz.sigmawords.data.model.Word

interface WordRepository {

    fun getWords(result: MutableLiveData<List<Word>>, wordsListName: String)
    fun getWordWithId(id: String, result: MutableLiveData<Word>, wordsListName: String)
}