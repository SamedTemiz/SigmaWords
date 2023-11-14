package com.samedtemiz.sigmawords.presentation.sign_in

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.samedtemiz.sigmawords.R
import com.samedtemiz.sigmawords.authentication.SignInState

@Composable
fun SignInScreen(
    state: SignInState,
    onSignInClick: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = state.isSignInError) {
        state.isSignInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
            elevation = CardDefaults.cardElevation(defaultElevation = 50.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxSize()
            ) {
                val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.google))

                LottieAnimation(
                    composition = composition, iterations = 1,
                    restartOnPlay = false,
                    isPlaying = true,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(300.dp, 150.dp)
                        .padding(top = 16.dp)
                )

                Card(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                ) {
                    Button(
                        onClick = { onSignInClick() },
                        modifier = Modifier
                            .fillMaxSize(),
                    ) {
                        Text(text = "GİRİŞ", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }

            }
        }
    }
}