package com.samedtemiz.sigmawords.presentation.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samedtemiz.sigmawords.data.model.Word
import com.samedtemiz.sigmawords.data.repository.word.WordRepository
import com.samedtemiz.sigmawords.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val wordRepository: WordRepository) : ViewModel() {

    private val _words = MutableLiveData<UiState<List<Word>>>()
    val words: LiveData<UiState<List<Word>>> = _words

    init {
        //getWords()
    }

    private fun getWords() {
        _words.value = UiState.Loading

        wordRepository.getAllWords(
            result = { _words.value = it },
            wordsListName = "AllWords"
        )
    }
}