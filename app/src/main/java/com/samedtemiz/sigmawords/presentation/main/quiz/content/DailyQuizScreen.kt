package com.samedtemiz.sigmawords.presentation.main.quiz.content

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.samedtemiz.sigmawords.R
import com.samedtemiz.sigmawords.data.model.Question
import com.samedtemiz.sigmawords.data.model.Quiz
import com.samedtemiz.sigmawords.presentation.Screen
import com.samedtemiz.sigmawords.presentation.main.quiz.QuizViewModel
import com.samedtemiz.sigmawords.util.UiState
import kotlinx.coroutines.launch

private const val TAG = "DailyQuiz"
@Composable
fun DailyQuizScreen(
    viewModel: QuizViewModel,
    navController: NavController
) {
    val quizState by viewModel.quiz.observeAsState()

    var progress by remember { mutableStateOf(0f) }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        quizState?.run {
            when (this) {
                is UiState.Loading -> {
                    CircularProgressIndicator()
                }

                is UiState.Failure -> {
                    Log.d("DailyQuizScreen", this.error.toString())
                }

                is UiState.Success -> {
                    val currentQuestionIndex = 1 // Şu anki soru indeksi
                    val totalQuestionsCount = data.questions!!.size // Toplam soru sayısı
                    progress = animateFloatAsState(
                        targetValue = (currentQuestionIndex.toFloat() * 100 / totalQuestionsCount) / 100,
                        label = ""
                    ).value

                    TopProgress(
                        questionIndex = currentQuestionIndex,
                        totalQuestionsCount = totalQuestionsCount,
                        progress = progress
                    )

                    QuizSection(questions = data.questions)
                }
            }
        }
    }
}

@Composable
fun TopProgress(
    questionIndex: Int,
    totalQuestionsCount: Int,
    progress: Float
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f)
    ) {
        // Top Section
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "$totalQuestionsCount questions",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.acherus_grotesque))
                )

                Card(shape = RoundedCornerShape(5.dp), modifier = Modifier.size(70.dp, 30.dp)) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "A1",
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.acherus_grotesque))
                        )
                    }
                }

                Card(shape = RoundedCornerShape(5.dp), modifier = Modifier.size(70.dp, 30.dp)) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "00:30",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.acherus_grotesque))
                        )
                    }
                }
            }
        }

        // Bottom Section
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp, 0.dp, 10.dp, 10.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Question ${questionIndex} of ${totalQuestionsCount}",
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.acherus_grotesque))
                )
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .height(20.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(7.dp))
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuizSection(questions: List<Question>) {
    var isDone by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val pagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f
        ) {
            questions.size
        }
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = Modifier.weight(1f),
        ) { page ->
            val question = questions[page]
            QuestionComponent(question = question) { index ->
                question.selectedOptionIndex = index
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    scope.launch {
                        if (pagerState.currentPage > 0) pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        Log.d(TAG, questions[pagerState.currentPage].toString())
                    }
                }
            ) {
                Text(text = "Geri")
            }

            if (isDone) {
                Button(
                    onClick = {}
                ) {
                    Text(text = "Bitir")
                }
            }

            Button(
                onClick = {
                    scope.launch {
                        if (pagerState.currentPage < questions.size - 1) pagerState.animateScrollToPage(
                            pagerState.currentPage + 1
                        )
                        Log.d(TAG, questions[pagerState.currentPage].toString())
                    }
                }
            ) {
                Text(text = "İleri")
            }
        }
    }
}

@Composable
fun QuestionComponent(
    question: Question,
    onOptionSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = question.questionTerm ?: "",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.acherus_grotesque)),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
        )
        question.options?.forEachIndexed { index, option ->
            OptionItem(
                optionTitle = option,
                isSelected = index == question.selectedOptionIndex,
                onOptionSelected = { onOptionSelected(index) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun OptionItem(
    optionTitle: String,
    isSelected: Boolean = false,
    onOptionSelected: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOptionSelected() }
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color.Gray else Color.Transparent
        ),
    ) {
        Text(
            text = optionTitle,
            modifier = Modifier.padding(16.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal
        )
    }
}