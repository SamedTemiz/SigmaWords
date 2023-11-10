package com.samedtemiz.sigmawords.presentation.main.quiz

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.samedtemiz.sigmawords.presentation.Screen
import com.samedtemiz.sigmawords.presentation.main.home.component.AnimatedShimmer
import com.samedtemiz.sigmawords.presentation.main.quiz.content.AlreadySolvedScreen
import com.samedtemiz.sigmawords.presentation.main.quiz.content.DailyQuizScreen
import com.samedtemiz.sigmawords.presentation.main.quiz.content.ResultScreen
import com.samedtemiz.sigmawords.presentation.main.quiz.content.StartQuizScreen
import com.samedtemiz.sigmawords.util.Options
import com.samedtemiz.sigmawords.util.UiState

private const val TAG = "Quiz Screen"
@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    navController: NavHostController = rememberNavController(),
) {
    val quizState by viewModel.quiz.observeAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        contentAlignment = Alignment.Center
    ) {
        quizState?.let { state ->
            when (state) {
                is UiState.Loading -> {
                    AnimatedShimmer(screen = "quiz")
                }

                is UiState.Failure -> {
                    val words by viewModel.words.observeAsState()
                    val sigmaWords by viewModel.sigmaWords.observeAsState()

                    var isQuizCreated by remember { mutableStateOf(false) }

                    if (!isQuizCreated && !words.isNullOrEmpty() && sigmaWords != null) {
                        LaunchedEffect(key1 = Unit) {
                            viewModel.createQuiz(Options.QUESTION_COUNT.get as Int)
                            isQuizCreated = true
                        }
                    }else{
                        Log.d("SAMED","becermedik")
                    }

                    viewModel.fetchWords(Options.ALL_WORDS.get.toString())
                }

                is UiState.Success -> {
                    Log.d(TAG, "Quiz verisi alındı.")
                    val isSolved = state.data.solved
                    NavHost(
                        navController = navController,
                        route = Screen.Main.Quiz.route,
                        startDestination = if (isSolved == false) Screen.Main.Quiz.Start.route else Screen.Main.Quiz.AlreadySolved.route
                    ) {
                        composable(Screen.Main.Quiz.Start.route) {
                            StartQuizScreen(navController = navController, questionCount = state.data.questions?.size ?: 0)
                        }

                        composable(Screen.Main.Quiz.AlreadySolved.route) {
                            AlreadySolvedScreen(navController)
                        }

                        composable(Screen.Main.Quiz.DailyQuiz.route) {
                            DailyQuizScreen(viewModel = viewModel, navController = navController)
                        }

                        composable(Screen.Main.Quiz.Result.route) {
                            LaunchedEffect(key1 = Unit){
                                viewModel.getCurrentResult(quizId = state.data.quizId ?: "")
                            }

                            ResultScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        } ?: run {
            AnimatedShimmer(screen = "quiz")
        }
    }
}