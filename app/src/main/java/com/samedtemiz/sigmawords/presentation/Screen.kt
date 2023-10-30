package com.samedtemiz.sigmawords.presentation

const val ON_BOARD_ROUTE = "on_board"


sealed class Screen(
    val route: String,
    val title: String
) {

    // Welcome Screens
    object Welcome : Screen(route = "welcome", title = "Welcome") {
        object Splash : Screen(route = "splash", title = "Splash")
        object OnBoard : Screen(route = "onboard", title = "On Board")
    }

    // Sign in screen
    object SignIn : Screen(route = "sign_in", title = "Sign in")


    // Main Screens
    object Main : Screen(route = "main", title = "Main") {
        object Home : Screen(route = "home", title = "Home")

        // Quiz Screens
        object Quiz : Screen(route = "quiz", title = "Quiz"){
            object Start: Screen(route = "start", title = "Start")
            object DailyQuiz: Screen(route = "dailyQuiz", title = "Daily Quiz")
            object Celebration: Screen(route = "celebration", title = "Celebration")
        }


        object Profile : Screen(route = "profile", title = "Profile")
    }

}
