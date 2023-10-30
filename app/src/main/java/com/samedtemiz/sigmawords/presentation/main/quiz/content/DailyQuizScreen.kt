package com.samedtemiz.sigmawords.presentation.main.quiz.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

@Composable
fun DailyQuizScreen(
    navController: NavController
) {
    var progress by remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val questionIndex = 2
        val totalQuestionsCount = 15
        progress = animateFloatAsState(
            targetValue = (questionIndex.toFloat() * 100 / totalQuestionsCount) / 100,
            label = ""
        ).value

//        quizState?.run {
//            when (this) {
//                is UiState.Loading -> {
//                    CircularProgressIndicator()
//                }
//
//                is UiState.Failure -> {
//
//                }
//
//                is UiState.Success -> {
//                    TopProgress(
//                        questionIndex = 2,
//                        totalQuestionsCount = 15,
//                        progress = progress
//                    )
//
//                    QuestionSection(
//                        this.data,
//                        onOptionSelected = {
//                            progress += 1.0F
//                        }
//                    )
//                }
//            }
//        }
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
fun QuestionSection(
    quizState: List<Question>?,
    onOptionSelected: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .fillMaxHeight(0.8f)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            quizState?.let { list ->
                val pagerState = rememberPagerState { list.size }

                HorizontalPager(state = pagerState) { page ->
                    QuestionComponent(
                        question = quizState[page].questionTerm,
                        options = quizState[page].options,
                        onOptionSelected = {}
                    )
                }
            }

        }
    }
}

@Composable
fun QuestionComponent(
    question: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = question,
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.acherus_grotesque)),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
        )
        options.forEach { option ->

            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    initialOffsetY = { it }
                ) + expandVertically(
                    expandFrom = Alignment.Top
                ) + fadeIn()
            ) {
                OptionItem(
                    option = option,
                    onOptionSelected = { onOptionSelected(it) }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionItem(option: String, onOptionSelected: (String) -> Unit) {
    Card(
        shape = CardDefaults.outlinedShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSecondaryContainer),
        modifier = Modifier.fillMaxWidth(),
        onClick = { onOptionSelected(option) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = option,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}