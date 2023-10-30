package com.samedtemiz.sigmawords.presentation.main.quiz.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.samedtemiz.sigmawords.R
import com.samedtemiz.sigmawords.presentation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartQuizScreen(
    navController: NavHostController
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.solving))

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            modifier = Modifier.size(200.dp),
            composition = composition,
            iterations = LottieConstants.IterateForever
        )

        Card(
            shape = CardDefaults.elevatedShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.fillMaxWidth(0.6f),
            onClick = {
                navController.popBackStack()
                navController.navigate(Screen.Main.Quiz.DailyQuiz.route)
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "TESTE BAŞLA",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Card(
            shape = CardDefaults.elevatedShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary
            ),
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.tally),
                        contentDescription = "Sigma",
                        modifier = Modifier.size(22.dp),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )

                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Soru Sayısı: 15",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }

                Divider(color = MaterialTheme.colorScheme.onSecondary, thickness = 1.dp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.sigma),
                        contentDescription = "Sigma",
                        modifier = Modifier.size(22.dp),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )

                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Sigma: 5",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
    }
}
