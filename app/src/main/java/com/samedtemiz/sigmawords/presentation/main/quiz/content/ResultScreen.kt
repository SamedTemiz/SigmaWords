package com.samedtemiz.sigmawords.presentation.main.quiz.content

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.samedtemiz.sigmawords.data.model.Result
import com.samedtemiz.sigmawords.presentation.main.quiz.QuizViewModel
import com.samedtemiz.sigmawords.util.UiState

private const val TAG = "ResultScreen"
@Composable
fun ResultScreen(viewModel: QuizViewModel) {
    val resultState by viewModel.result.observeAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        resultState?.run {
            when(this){
                is UiState.Loading -> {
                    CircularProgressIndicator()
                }

                is UiState.Success ->{
                    ResultCard(data)
                }

                is UiState.Failure -> {
                    Log.d(TAG, error.toString())
                }
            }
        }

    }
}

@Composable
fun ResultCard(result: Result){
    Column {
        Text(text = result.quizId.toString())
        Text(text = result.questionCount.toString())
        Text(text = result.correctCount.toString())

        LazyColumn() {
            items(result.wrongAnswers?.size ?: 0) { index ->
                // Listenin her bir elemanı için gerekli işlemleri gerçekleştir
                val word = result.wrongAnswers?.get(index)
                // Örneğin:
                word?.let {
                    Text(text = "Question: ${it.term}, Answer: ${it.meaning}")
                }

            }
        }

    }
}