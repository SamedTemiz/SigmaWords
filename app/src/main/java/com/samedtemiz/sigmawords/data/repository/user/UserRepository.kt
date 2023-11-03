package com.samedtemiz.sigmawords.data.repository.user

import androidx.lifecycle.MutableLiveData
import com.samedtemiz.sigmawords.data.model.Quiz
import com.samedtemiz.sigmawords.data.model.Result
import com.samedtemiz.sigmawords.data.model.User
import com.samedtemiz.sigmawords.data.model.Word
import com.samedtemiz.sigmawords.util.UiState

interface UserRepository {

    // Quiz Operations
    fun getQuiz(userId: String, quiz: MutableLiveData<UiState<Quiz>>)
    fun addQuiz(userId: String, quiz: Quiz)
    fun updateQuiz(userId: String, quiz: Quiz)


    // Quiz Result Operations
    fun addResult(userId: String, result: Result)
    fun getResult(userId: String, quizId: String, result: MutableLiveData<UiState<Result>>)


    // User Database Operations
    fun createUserDatabase(user: User)
}