package com.samedtemiz.sigmawords.presentation.main.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.samedtemiz.sigmawords.data.model.Word
import com.samedtemiz.sigmawords.data.repository.user.UserRepository
import com.samedtemiz.sigmawords.data.repository.word.WordRepository
import com.samedtemiz.sigmawords.util.UiState
import com.samedtemiz.sigmawords.data.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _resultState = MutableLiveData<UiState<List<Result>>>()
    val resultState: LiveData<UiState<List<Result>>> = _resultState

    init {
        _resultState.value = UiState.Loading
        _resultState.observeForever {
            when (it) {
                is UiState.Success -> {
                    Log.d(TAG, "Veriler alındı: ${it.data.get(0).quizId}")
                }

                is UiState.Failure -> {
                    Log.d(TAG, it.error.toString())
                }

                else -> {
                    Log.d(TAG, "Something wrong")
                }
            }
        }

        getResultList()
    }


    private fun getResultList() {
        userRepository.getResultList(
            userId = Firebase.auth.uid.toString(),
            result = _resultState
        )
    }
}