package com.samedtemiz.sigmawords.presentation.main.home

import androidx.compose.foundation.background
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.samedtemiz.sigmawords.R
import com.samedtemiz.sigmawords.data.model.User
import com.samedtemiz.sigmawords.util.UiState
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    userData: User?
) {
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
                    HeaderSection(user)
                }

                ContentSection(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HeaderSection(user: User) {
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
                        text = "Hoş geldiniz", fontSize = 18.sp,
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
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                // Sol Taraf

            }

            Divider(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxHeight()  //fill the max height
                    .width(2.dp)
                    .padding(vertical = 10.dp)
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                // Sağ taraf

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
                        Column {
                            repeat(6) {
                                AnimatedShimmer("home")
                            }
                        }
                    }

                    is UiState.Failure -> {
                        NoResultScreen()
                    }

                    is UiState.Success -> {
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
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
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
}

fun String.formatDate(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("tr", "TR"))

    val date = inputFormat.parse(this)
    return outputFormat.format(date!!)
}