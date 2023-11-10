package com.samedtemiz.sigmawords.presentation.main.quiz.content

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.samedtemiz.sigmawords.data.model.Result
import com.samedtemiz.sigmawords.presentation.main.quiz.QuizViewModel
import com.samedtemiz.sigmawords.util.UiState

private const val TAG = "ResultScreen"

@Composable
fun ResultScreen(resultState: UiState<Result>) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (resultState) {
            is UiState.Loading -> {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
            }

            is UiState.Success -> {
                Log.d(TAG, "Result verisi alındı.")
                ResultCard(resultState.data)
            }

            is UiState.Failure -> {
                Log.d(TAG, resultState.error.toString())
            }
        }
    }
}

@Composable
fun ResultCard(result: Result) {
    Column {
        Text(text = result.quizId.toString())
        Text(text = result.questionCount.toString())
        Text(text = result.correctCount.toString())

        val wrongAnswers = result.wrongAnswers
        wrongAnswers?.let { list ->
            LazyColumn() {
                items(list.size) { index ->
                    val word = list[index]

                    Text(text = "Question: ${word.term}, Answer: ${word.meaning}")
                }
            }
        }
    }
}