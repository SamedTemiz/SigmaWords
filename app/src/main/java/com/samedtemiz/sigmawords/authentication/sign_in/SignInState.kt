package com.samedtemiz.sigmawords.authentication.sign_in

data class SignInState(
    val isSignInSuccessful : Boolean = false,
    val isSignInError: String? = null
)