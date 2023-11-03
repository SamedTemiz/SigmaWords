package com.samedtemiz.sigmawords.presentation.main.quiz.content

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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

    var showResultDialog by remember { mutableStateOf(false) }

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
                    val totalQuestionsCount = data.questions!!.size // Toplam soru sayısı
                    var currentQuestionIndex by remember { mutableIntStateOf(1) }

                    TopProgress(
                        currentQuestionIndex = currentQuestionIndex,
                        totalQuestionsCount = totalQuestionsCount
                    )

                    QuizSection(
                        questions = data.questions,
                        progress = { value ->
                            currentQuestionIndex = value
                        },
                        result = {
                            viewModel.createResult(data)
                            showResultDialog = true
                        }
                    )
                }
            }
        }

        if(showResultDialog){
            AlertDialog(
                onDismissRequest = { showResultDialog = false },
                title = { Text("Tebrikler") },
                text = { Text("Günlük test tamamlandı. Her gün düzenli olarak testlere katılman çok önemli. Şimdi sonucu öğren!") },
                confirmButton = {
                    Button(
                        onClick = {
                            showResultDialog = false

                            navController.popBackStack()
                            navController.navigate(Screen.Main.Quiz.Result.route)
                        }
                    ) {
                        Text("Sonuç")
                    }
                }
            )
        }
    }
}

@Composable
fun TopProgress(
    currentQuestionIndex: Int,
    totalQuestionsCount: Int
) {
    var progress by remember { mutableFloatStateOf(currentQuestionIndex.toFloat() / totalQuestionsCount) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        label = ""
    )

    LaunchedEffect(currentQuestionIndex, totalQuestionsCount) {
        progress = currentQuestionIndex.toFloat() / totalQuestionsCount
    }

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

                Card(shape = RoundedCornerShape(6.dp), modifier = Modifier.size(70.dp, 30.dp)) {
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

                Card(shape = RoundedCornerShape(6.dp), modifier = Modifier.size(70.dp, 30.dp)) {
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
                .padding(horizontal = 10.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Question $currentQuestionIndex of $totalQuestionsCount",
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.acherus_grotesque))
                )

                LinearProgressIndicator(
                    progress = animatedProgress,
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
fun QuizSection(
    questions: List<Question>,
    progress: (Int) -> Unit,
    result: () -> Unit
) {
    var selectedOption by remember { mutableStateOf("") }
    var isDone by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
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
        ) { page ->
            val question = questions[page]

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Card(
                    shape = CardDefaults.elevatedShape,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                ) {
                    Text(
                        text = question.questionWord?.term ?: "",
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = FontFamily(Font(R.font.acherus_grotesque)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }

                question.options?.forEachIndexed { index, option ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        shape = CardDefaults.elevatedShape,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
                        colors = CardDefaults.cardColors(
                            containerColor = if (option == selectedOption) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                            contentColor = if (option == selectedOption) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (option == selectedOption),
                                    onClick = {
                                        selectedOption = option
                                        question.selectedOptionIndex = index
                                        Log.d(TAG, "SEÇİLEN: $index")
                                    }
                                )
                                .padding(10.dp)
                        ) {
                            RadioButton(
                                selected = (option == selectedOption),
                                onClick = {
                                    selectedOption = option
                                    question.selectedOptionIndex = index
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colorScheme.primary,
                                    unselectedColor = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Text(
                                text = option,
                                fontSize = 18.sp,
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .align(Alignment.CenterVertically)
                            )
                        }
                    }

                }
            }

        }

        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier.fillMaxWidth(),
        ) {
            isDone = pagerState.currentPage >= questions.size - 1
            ElevatedButton(
                onClick = {
                    if (isDone) {
                        result()
                        Log.d(TAG, "Quiz tamamlandı.")
                    } else {
                        scope.launch {
                            if (pagerState.currentPage < questions.size - 1) {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }

                        progress(pagerState.currentPage+2)
                    }
                    selectedOption = ""
                },
                enabled = selectedOption.isNotBlank() || pagerState.currentPage >= questions.size,
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    if (pagerState.currentPage < questions.size - 1) "Sonraki" else "Bitir",
                    fontSize = 18.sp
                )
            }
        }
    }
}




