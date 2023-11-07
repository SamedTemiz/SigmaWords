package com.samedtemiz.sigmawords.presentation.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.samedtemiz.sigmawords.presentation.main.home.component.AnimatedShimmer
import com.samedtemiz.sigmawords.presentation.main.home.component.ExpandableResultCard
import com.samedtemiz.sigmawords.util.UiState

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
                        text = "Geri dönmene sevindim", fontSize = 18.sp,
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
//                    Image(
//                        painterResource(id = R.drawable.seftali),
//                        contentDescription = "Profile Picture",
//                        modifier = Modifier
//                            .clip(CircleShape)
//                            .size(60.dp),
//                        contentScale = ContentScale.FillWidth,
//                    )
                }
            }
        }

        // Bottom Section
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp, 0.dp, 10.dp, 10.dp)
                .background(Color.DarkGray)
        ) {

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
            text = "Sonuçlar",
            fontSize = 21.sp,
            fontFamily = FontFamily(Font(R.font.acherus_grotesque)),
            textAlign = TextAlign.Left,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
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
                                AnimatedShimmer()
                            }
                        }
                    }

                    is UiState.Failure -> {
                        Text(text = this.error.toString())
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
                                    title = singleResult.resultDate.toString(),
                                    wordList = singleResult.wrongAnswers
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}