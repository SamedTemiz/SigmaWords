package com.samedtemiz.sigmawords.presentation.main.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.samedtemiz.sigmawords.R
import com.samedtemiz.sigmawords.data.model.User
import com.samedtemiz.sigmawords.util.UiState
import java.text.SimpleDateFormat
import java.util.Locale

private const val TAG = "Home Screen"

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    userData: User?
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.checkDailyQuizStatus()
        viewModel.getResultList()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {
            Column {
                userData?.let { user ->
                    HeaderSection(user, viewModel = viewModel)
                }

                ContentSection(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HeaderSection(user: User, viewModel: HomeViewModel) {
    val quizStatus by viewModel.dailyQuiz.observeAsState()
    val successStatus by viewModel.successRate.observeAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
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
                Column() {
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 24.sp,
                                    fontFamily = FontFamily(Font(R.font.acherus_grotesque))
                                )
                            ) {
                                append("Merhaba, ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily(Font(R.font.acherus_grotesque))
                                )
                            ) {
                                user.username?.let {
                                    append(it.split(" ")[0])
                                } ?: "NO-DATA"
                            }
                        }
                    )
                    Text(
                        text = "Hoş geldin", fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.acherus_grotesque))
                    )
                }
                Box(
                    contentAlignment = Alignment.Center,
                ) {
                    GlideImage(
                        model = user.profilePictureUrl,
                        contentDescription = "Google Account Profile Picture",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(70.dp),
                        contentScale = ContentScale.FillWidth,
                    )
                }
            }
        }

        // Bottom Section
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp, 0.dp, 10.dp, 10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))

            // Sol Taraf
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    quizStatus?.let { state ->
                        when (state) {
                            is UiState.Loading -> {
                                LottieAnimation(
                                    modifier = Modifier.fillMaxSize(),
                                    composition = composition,
                                    iterations = LottieConstants.IterateForever,
                                )
                            }

                            is UiState.Failure -> {
                                Log.d(TAG, state.error.toString())
                            }

                            is UiState.Success -> {
                                if (state.data) {
                                    val doneComposition by rememberLottieComposition(
                                        LottieCompositionSpec.RawRes(R.raw.done)
                                    )
                                    LottieAnimation(
                                        modifier = Modifier.fillMaxSize(),
                                        composition = doneComposition,
                                        iterations = 1,
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    val doneComposition by rememberLottieComposition(
                                        LottieCompositionSpec.RawRes(R.raw.not_done)
                                    )
                                    LottieAnimation(
                                        modifier = Modifier.fillMaxSize(),
                                        composition = doneComposition,
                                        iterations = 1,
                                        contentScale = ContentScale.Fit,
                                    )
                                }
                            }
                        }
                    }
                }

                Text(
                    text = "Günlük Test",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.acherus_grotesque)),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }

            Divider(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxHeight()  //fill the max height
                    .width(2.dp)
                    .padding(vertical = 10.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(10.dp)
            ) {
                successStatus?.let { state ->
                    when (state) {
                        is UiState.Loading -> {
                            LottieAnimation(
                                modifier = Modifier.fillMaxSize(),
                                composition = composition,
                                iterations = LottieConstants.IterateForever,
                            )
                        }

                        is UiState.Failure -> {
                            Text(
                                text = "Başarı oranı hesaplanamadı!",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                fontFamily = FontFamily(Font(R.font.acherus_grotesque)),
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }

                        is UiState.Success -> {
                            SuccessInformation(state.data)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ContentSection(viewModel: HomeViewModel) {
    val resultState by viewModel.resultState.observeAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Geçmiş Sonuçlar",
            fontSize = 21.sp,
            fontFamily = FontFamily(Font(R.font.acherus_grotesque)),
            textAlign = TextAlign.Left,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )

        Divider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            resultState?.run {
                when (this) {
                    is UiState.Loading -> {
                        Log.d(TAG, "Result verileri bekleniyor.")
                        Column {
                            repeat(6) {
                                AnimatedShimmer("home")
                            }
                        }
                    }

                    is UiState.Failure -> {
                        Log.d(TAG, this.error.toString())
                        viewModel.calculateSuccessRate(null)

                        NoResultScreen()
                    }

                    is UiState.Success -> {
                        Log.d(TAG, "Result verileri alındı.")
                        viewModel.calculateSuccessRate(data)

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(
                                bottom = 24.dp
                            )
                        ) {
                            items(data.size) { index ->
                                val singleResult = data[index]
                                ExpandableResultCard(
                                    title = singleResult.resultDate?.formatDate().toString(),
                                    wordList = singleResult.wrongAnswers,
                                    questionCount = singleResult.questionCount,
                                    correctCount = singleResult.correctCount
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NoResultScreen() {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = R.drawable.no_results),
                contentDescription = "No Result",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(100.dp)
                    .padding(top = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Sonuç listesi boş",
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.acherus_grotesque))
            )
        }

        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.down_arrow))
        LottieAnimation(
            modifier = Modifier.size(150.dp),
            composition = composition,
            iterations = LottieConstants.IterateForever,
            speed = 1.5f,
        )
    }
}

@Composable
fun SuccessInformation(data: Pair<String, String>) {
    Row {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .weight(2f)
        ) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.chart))
            LottieAnimation(
                modifier = Modifier.fillMaxSize(),
                composition = composition,
                iterations = LottieConstants.IterateForever,
                contentScale = ContentScale.Fit,
            )
        }
        Column(
            modifier = Modifier
                .weight(4f)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.acherus_grotesque_bold)),
                        )
                    ) {
                        append("%${data.first}")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.acherus_grotesque)),
                        )
                    ) {
                        append("\n/${data.second}")
                    }
                },
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        var displayPopup by remember { mutableStateOf(false) }
        Icon(
            painter = painterResource(id = R.drawable.question),
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .clickable {
                    displayPopup = true
                }
        )

        if (displayPopup) {
            AlertDialog(
                onDismissRequest = { displayPopup = false },
                title = { Text("Başarı oranı nedir?") },
                text = { Text("Günlük testlerde doğru cevaplanan kelimelerin toplam sorulan kelime sayısına oranı, başarı oranını ifade eder. Örneğin, günlük testlerde toplamda 20 kelime sorulmuşsa ve bu 20 kelimenin 18'i doğru cevaplanmışsa, başarı oranımız %90 olacaktır. Diğer bir ölçüm ise bugüne kadar çözülen test sayısını gösterir. Bu sayede çözülen test sayısına göre başarı oranımız ekranda belirtilir.") },
                confirmButton = {
                    Button(
                        onClick = {
                            displayPopup = false
                        }
                    ) {
                        Text("Tamam")
                    }
                }
            )
        }
    }
}

fun String.formatDate(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("tr", "TR"))

    val date = inputFormat.parse(this)
    return outputFormat.format(date!!)
}