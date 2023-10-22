package com.samedtemiz.sigmawords.authentication

data class SignInState(
    val isSignInSuccessful : Boolean = false,
    val isSignInError: String? = null
)