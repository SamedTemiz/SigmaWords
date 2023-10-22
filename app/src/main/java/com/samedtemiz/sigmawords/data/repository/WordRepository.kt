package com.samedtemiz.sigmawords.data.repository

import com.samedtemiz.sigmawords.data.model.Word
import com.samedtemiz.sigmawords.util.UiState

interface WordRepository {

    fun getWords(result: (UiState<List<Word>>) -> Unit)
}