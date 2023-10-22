package com.samedtemiz.sigmawords.presentation

sealed class Screen(
    val route: String,
    val title: String
){
    object SignIn: Screen(
        route = "sign_in",
        title = "Sign in"
    )
    object Profile: Screen(
        route = "profile",
        title = "Profile"
    )
    object Main: Screen(
        route = "main",
        title = "Main"
    )
}
