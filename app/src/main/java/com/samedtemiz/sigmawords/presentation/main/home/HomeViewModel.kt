package com.samedtemiz.sigmawords.presentation.main.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    private val userId: State<String> = mutableStateOf(Firebase.auth.currentUser?.uid.toString())

    private val _resultState = MutableLiveData<UiState<List<Result>>>()
    val resultState: LiveData<UiState<List<Result>>> = _resultState

    val dailyQuiz = MutableLiveData<UiState<Boolean>>()
    val successRate: MutableLiveData<UiState<Pair<String, String>>> = MutableLiveData()

    init {
        successRate.value = UiState.Loading
        dailyQuiz.value = UiState.Loading
        _resultState.value = UiState.Loading
    }

    fun calculateSuccessRate(list: List<Result>?) {
        if (!list.isNullOrEmpty()) {
            val resultCount = list.size

            var totalQuestion = 0
            var correctCount = 0
            list.forEach { result ->
                totalQuestion += result.questionCount ?: 0
                correctCount += result.correctCount ?: 0
            }

            val rate = (correctCount * 100).toFloat() / totalQuestion
            successRate.value = UiState.Success(Pair("${rate.roundToInt()}", "$resultCount"))
        } else {
            successRate.value = UiState.Failure("Veri Bulunamadı")
        }
    }

    fun checkDailyQuizStatus() {
        CoroutineScope(Dispatchers.IO).launch {
            userRepository.checkQuiz(userId = userId.value, dailyQuiz)
        }
    }

    fun getResultList() {
        userRepository.getResultList(
            userId = userId.value,
            result = _resultState
        )
    }
}