package com.samedtemiz.sigmawords.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.samedtemiz.sigmawords.R
import com.samedtemiz.sigmawords.presentation.ui.theme.SigmaWordsTheme

@Composable
fun UnknownErrorComponent(error: String? = "Bilinmeyen hata!") {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.unknown_error))
        LottieAnimation(
            modifier = Modifier.size(200.dp),
            composition = composition,
            iterations = LottieConstants.IterateForever
        )

        Spacer(Modifier.height(16.dp))
        Text(text = error!!, fontSize = 24.sp, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Preview
@Composable
fun PreviewError() {
    SigmaWordsTheme {
        UnknownErrorComponent()
    }
}