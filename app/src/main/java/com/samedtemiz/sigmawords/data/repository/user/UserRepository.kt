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
    fun checkQuiz(userId: String, dailyQuiz: MutableLiveData<UiState<Boolean>>)


    // Sigma Operations
    fun getUserSigmaWords(userId: String, currentDate: String, result: MutableLiveData<List<Word>>)
    fun addSigmaWords(userId: String, sigmaWords: List<Word>)
    fun deleteSigmaWords(userId: String, forDeleteSigmaWords: List<Word>)


    // Quiz Result Operations
    fun addResult(userId: String, result: Result)
    fun getCurrentResult(userId: String, quizId: String, result: MutableLiveData<UiState<Result>>)
    fun getResultList(userId: String, result: MutableLiveData<UiState<List<Result>>>)


    // User Operations
    fun createUserDatabase(user: User)
    fun getUserDatabase(userId: String, userData: MutableLiveData<UiState<User>>)
    fun deleteUserDatabase(userId: String) : Boolean
    fun deleteUser(userId: String) : Boolean
}