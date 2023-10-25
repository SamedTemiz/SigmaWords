package com.samedtemiz.sigmawords.authentication

data class SignInResult(
    val data: com.samedtemiz.sigmawords.data.model.User?,
    val errorMessage: String?
)

