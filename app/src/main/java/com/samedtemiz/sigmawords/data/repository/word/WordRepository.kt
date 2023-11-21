package com.samedtemiz.sigmawords.data.repository.word

import com.samedtemiz.sigmawords.data.dao.WordDao
import com.samedtemiz.sigmawords.data.model.Word
import javax.inject.Inject


class WordRepository @Inject constructor(private val wordDao: WordDao) {

    suspend fun insertAllWords(words: List<Word>){
        wordDao.insertAll(words = words)
    }

    fun getAllWords() = wordDao.getAllWords()
}