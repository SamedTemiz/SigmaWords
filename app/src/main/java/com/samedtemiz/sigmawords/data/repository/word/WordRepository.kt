package com.samedtemiz.sigmawords.data.repository.word

import com.samedtemiz.sigmawords.data.model.Word
import com.samedtemiz.sigmawords.util.UiState

interface WordRepository {

    fun getAllWords(result: (UiState<List<Word>>) -> Unit, wordsListName: String)
}