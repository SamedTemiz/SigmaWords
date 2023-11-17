package com.samedtemiz.sigmawords.presentation.main.quiz.content

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.samedtemiz.sigmawords.R
import com.samedtemiz.sigmawords.data.model.Result
import com.samedtemiz.sigmawords.data.model.Word
import com.samedtemiz.sigmawords.presentation.main.home.formatDate
import com.samedtemiz.sigmawords.presentation.main.quiz.QuizViewModel
import com.samedtemiz.sigmawords.util.UiState

private const val TAG = "ResultScreen"

@Composable
fun ResultScreen(viewModel: QuizViewModel) {
    val resultState by viewModel.result.observeAsState()
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        resultState?.let { state ->
            when (state) {
                is UiState.Loading -> {
                    Column {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
                        Text(
                            text = "Sonuç hesaplanıyor...",
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }

                is UiState.Success -> {
                    Log.d(TAG, "Result verisi alındı.")
                    ResultCard(state.data)
                }

                is UiState.Failure -> {
                    Log.d(TAG, state.error.toString())
                }
            }
        }
    }
}

@Composable
fun ResultCard(result: Result) {
    Box() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .padding(16.dp)
                .align(Alignment.TopCenter),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = result.resultDate?.formatDate().toString(),
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.acherus_grotesque_bold))
            )
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.confetti))
            LottieAnimation(
                modifier = Modifier.fillMaxSize(),
                composition = composition,
                iterations = 1,
                contentScale = ContentScale.Crop
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 100.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Text(
                    text = "TEBRİKLER",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.acherus_grotesque_bold))
                )
                Text(
                    text = "Günlük test tamamlandı",
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.acherus_grotesque))
                )
                Text(
                    text = "- ${result.questionCount} soru -",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.acherus_grotesque_bold))
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.correct),
                            contentDescription = "",
                            modifier = Modifier.size(45.dp)
                        )
                        Text(
                            text = "${result.correctCount}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.acherus_grotesque_bold))
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.wrong),
                            contentDescription = "",
                            modifier = Modifier.size(45.dp)
                        )
                        Text(
                            text = "${result.wrongAnswers?.size ?: 0}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.acherus_grotesque_bold))
                        )
                    }
                }

                result.wrongAnswers?.let {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            text = "Yanlış cevaplar:",
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.acherus_grotesque))
                        )
                        Column(
                            Modifier
                                .padding(top = 10.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            result.wrongAnswers?.forEach { word ->
                                WrongWord(word = word)
                            }
                        }
                    }
                } ?: run {
                    Text(
                        text = "YANLIŞ CEVAP YOK",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.acherus_grotesque_bold)),
                        modifier = Modifier.padding(top = 100.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun WrongWord(word: Word) {
    Column(
        Modifier
            .padding(bottom = 10.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            Text(
                text = word.term ?: "",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.acherus_grotesque)),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = word.category ?: "",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.acherus_grotesque)),
                maxLines = 1,
            )
        }
        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSecondaryContainer)
        Column(Modifier.padding(horizontal = 10.dp)) {
            Text(
                text = word.meaning ?: "",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.acherus_grotesque)),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = word.meaning2 ?: "",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.acherus_grotesque)),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
