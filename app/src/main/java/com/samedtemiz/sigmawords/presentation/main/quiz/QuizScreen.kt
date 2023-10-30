package com.samedtemiz.sigmawords.presentation.main.quiz

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.samedtemiz.sigmawords.presentation.Screen
import com.samedtemiz.sigmawords.presentation.main.quiz.content.CelebrationScreen
import com.samedtemiz.sigmawords.presentation.main.quiz.content.DailyQuizScreen
import com.samedtemiz.sigmawords.presentation.main.quiz.content.StartQuizScreen
import com.samedtemiz.sigmawords.presentation.ui.theme.SigmaWordsTheme

@Composable
fun QuizScreen(
//    viewModel: QuizViewModel,
    navController: NavHostController = rememberNavController(),
) {
    var isSolved by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        contentAlignment = Alignment.Center
    ) {
        NavHost(
            navController = navController,
            route = Screen.Main.Quiz.route,
            startDestination = Screen.Main.Quiz.Start.route
        ) {
            composable(route = Screen.Main.Quiz.Start.route) {
                StartQuizScreen(navController = navController)
            }

            composable(route = Screen.Main.Quiz.Celebration.route) {
                CelebrationScreen(navController = navController)
            }

            composable(route = Screen.Main.Quiz.DailyQuiz.route) {
                DailyQuizScreen(navController = navController)
            }
        }

    }
}