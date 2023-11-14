package com.samedtemiz.sigmawords.presentation.welcome

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.samedtemiz.sigmawords.R
import com.samedtemiz.sigmawords.presentation.Screen
import com.samedtemiz.sigmawords.presentation.ui.theme.SigmaWordsTheme
import kotlinx.coroutines.launch

//@Preview()
//@Preview(uiMode = UI_MODE_NIGHT_YES)
//@Composable
//fun OnBoardingPreview() {
//    SigmaWordsTheme {
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colorScheme.background
//        ) {
//            OnboardingScreen()
//        }
//    }
//}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(
    navController: NavHostController,
    onBoardViewModel: OnBoardViewModel = hiltViewModel()
) {
    val animations = listOf(
        R.raw.welcome_user,
        R.raw.what_is_it,
        R.raw.progress
    )
    val pageTitle = listOf(
        "Hoş Geldiniz!",
        "Sigma Algoritması Nedir?",
        "Sigma Aşamaları"
    )

    val titles = listOf(
        "Sigma Algoritması ile Tanışın",
        "Doğru Cevapların Gücü",
        "Bilgiyi Pekiştirme Yolları"
    )

    val descriptions = listOf(
        "Bu uygulama, öğrenme sürecinizi daha etkili hale getiren Sigma Algoritması'na dayanmaktadır.",
        "Her doğru cevap, öğrenme sürecinizi kişiselleştiren Sigma aşamalarında ilerlemeye neden olur.",
        "Sigma Algoritması, her kelimenin öğrenme yolunu özel hale getirir ve sık sık sorulan kelimeleri doğru cevaplandıkça aralıklarla tekrar sorar."
    )
    val pagerState = rememberPagerState(
        pageCount = animations.size
    )


    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .padding(26.dp)
        ) { currentPage ->
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animations[currentPage]))
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .size(350.dp, 300.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f))
                )

                Column(
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = pageTitle[currentPage],
                        Modifier
                            .fillMaxWidth()
                            .height(75.dp)
                            .padding(top = 10.dp)
                            .weight(1f),
                        textAlign = TextAlign.End,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.acherus_grotesque_bold))
                    )
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.acherus_grotesque_bold)),
                                    textDecoration = TextDecoration.Underline
                                )
                            ) {
                                append(titles[currentPage])
                            }

                            withStyle(
                                style = SpanStyle(
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily(Font(R.font.acherus_grotesque)),
                                )
                            ) {
                                append("\n\n" + descriptions[currentPage])
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(2f),
                    )
                }
            }
        }

        PageIndicator(
            pageCount = animations.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier
        )

        ButtonsSection(
        pagerState = pagerState,
        navController = navController,
        onBoardViewModel = onBoardViewModel
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ButtonsSection(
    pagerState: PagerState,
    navController: NavHostController,
    onBoardViewModel: OnBoardViewModel
) {

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(26.dp)
    ) {
        if (pagerState.currentPage != 2) {
            Text(
                text = "İleri",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .clickable {
                        scope.launch {
                            val nextPage = pagerState.currentPage + 1
                            pagerState.scrollToPage(nextPage)
                        }
                    },
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Geri",
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .clickable {
                        scope.launch {
                            val prevPage = pagerState.currentPage - 1
                            if (prevPage >= 0) {
                                pagerState.scrollToPage(prevPage)
                            }
                        }
                    },
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        } else {
            OutlinedButton(
                onClick = {
                    onBoardViewModel.saveOnBoardingState(completed = true)
                    navController.popBackStack()
                    navController.navigate(Screen.SignIn.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter), colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Başlayalım",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun PageIndicator(pageCount: Int, currentPage: Int, modifier: Modifier) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        repeat(pageCount) {
            IndicatorSingleDot(isSelected = it == currentPage)
        }
    }
}

@Composable
fun IndicatorSingleDot(isSelected: Boolean) {

    val width = animateDpAsState(targetValue = if (isSelected) 35.dp else 15.dp, label = "")
    Box(
        modifier = Modifier
            .padding(2.dp)
            .height(15.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer)
    )
}