package com.samedtemiz.sigmawords.data.repository.user

import androidx.lifecycle.MutableLiveData
import com.samedtemiz.sigmawords.data.model.Quiz
import com.samedtemiz.sigmawords.data.model.User
import com.samedtemiz.sigmawords.data.model.Word
import com.samedtemiz.sigmawords.util.UiState

interface UserRepository {

    // Quiz Operations
    fun getQuiz(userId: String, quiz: MutableLiveData<UiState<Quiz>>)
    fun addQuiz(userId: String, quiz: Quiz)
    fun updateQuiz(userId: String, solved: Boolean)


    // User Database Operations
    fun createUserDatabase(user: User)
}