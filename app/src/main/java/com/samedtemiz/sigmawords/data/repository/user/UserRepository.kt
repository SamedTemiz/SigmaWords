package com.samedtemiz.sigmawords.data.repository.user

import androidx.lifecycle.MutableLiveData
import com.samedtemiz.sigmawords.data.model.User
import com.samedtemiz.sigmawords.data.model.Word

interface UserRepository {

    fun createUserDatabase(user: User)
    fun checkUserDatabaseExist(id: String) : Boolean
    fun updateProgress(userId: String, sigmaWords: List<Word>)


    fun dailyQuizStatus(userId: String, currentDate: String, isSolved: MutableLiveData<Boolean>)
    fun updateQuizStatus(userId: String, isSolved: Boolean, solvedDate: String)
}