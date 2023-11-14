package com.samedtemiz.sigmawords.authentication

import com.samedtemiz.sigmawords.data.model.User

data class SignInResult(
    val data: User?,
    val errorMessage: String?
)

