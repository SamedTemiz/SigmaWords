package com.samedtemiz.sigmawords.data.repository.word

import com.samedtemiz.sigmawords.data.api.WordService
import com.samedtemiz.sigmawords.data.dao.WordDao
import com.samedtemiz.sigmawords.data.model.Word
import javax.inject.Inject


class WordRepository @Inject constructor(private val wordDao: WordDao, private val api: WordService) {

    suspend fun insertAllWords(words: List<Word>){
        wordDao.insertAll(words = words)
    }

    suspend fun getAllWordsFromApi() = api.getAllWords()

    fun getAllWordsFromRoom() = wordDao.getAllWords()
}