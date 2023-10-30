package com.samedtemiz.sigmawords.data.repository.user

import com.samedtemiz.sigmawords.data.model.User

interface UserRepository {

    fun createUserDatabase(user: User)
    fun checkUserDatabaseExist(id: String) : Boolean

}