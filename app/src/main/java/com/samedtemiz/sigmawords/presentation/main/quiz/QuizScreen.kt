package com.samedtemiz.sigmawords.presentation.main.quiz

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.samedtemiz.sigmawords.presentation.Screen
import com.samedtemiz.sigmawords.presentation.main.quiz.content.AlreadySolvedScreen
import com.samedtemiz.sigmawords.presentation.main.quiz.content.DailyQuizScreen
import com.samedtemiz.sigmawords.presentation.main.quiz.content.ResultScreen
import com.samedtemiz.sigmawords.presentation.main.quiz.content.StartQuizScreen

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    navController: NavHostController = rememberNavController(),
) {
    val isSolved by viewModel.isSolved.observeAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isSolved != null) {
            NavHost(
                navController = navController,
                route = Screen.Main.Quiz.route,
                startDestination = if(isSolved == false) Screen.Main.Quiz.Start.route else Screen.Main.Quiz.AlreadySolved.route
            ) {
                composable(Screen.Main.Quiz.Start.route) {
                    StartQuizScreen(
                        viewModel = viewModel,
                        navController = navController
                    )
                }

                composable(Screen.Main.Quiz.AlreadySolved.route) {
                    AlreadySolvedScreen()
                }

                composable(Screen.Main.Quiz.DailyQuiz.route) {
                    DailyQuizScreen(
                        viewModel = viewModel,
                        navController = navController
                    )
                }

                composable(Screen.Main.Quiz.Result.route){
                    ResultScreen(viewModel = viewModel)
                }
            }
        } else {
            CircularProgressIndicator()
        }

    }
}