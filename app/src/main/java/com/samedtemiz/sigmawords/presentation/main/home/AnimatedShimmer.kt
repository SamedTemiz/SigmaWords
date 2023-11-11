package com.samedtemiz.sigmawords.presentation.main.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedShimmer(screen: String) {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    val transition = rememberInfiniteTransition(label = "")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    if (screen == "home") {
        HomeShimmerGridItem(brush = brush)
    } else if (screen == "quiz") {
        QuizShimmerGridItem(brush = brush)
    }

}

@Composable
fun HomeShimmerGridItem(brush: Brush) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(brush)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(verticalArrangement = Arrangement.Center) {
            Spacer(
                modifier = Modifier
                    .height(17.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth(fraction = 0.7f)
                    .background(brush)
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Spacer(
                modifier = Modifier
                    .height(17.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth(fraction = 0.9f)
                    .background(brush)
            )
        }
    }
}

@Composable
fun QuizShimmerGridItem(brush: Brush) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .padding(horizontal = 10.dp)
        ) {
            Row() {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(30.dp)
                        .padding(horizontal = 10.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(brush)
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(30.dp)
                        .padding(horizontal = 10.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(brush)
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .padding(horizontal = 10.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(brush)
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(30.dp)
                    .padding(horizontal = 10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(brush)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .padding(horizontal = 10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(brush)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(horizontal = 10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(brush)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .padding(horizontal = 10.dp, vertical = 20.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(brush)
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(brush)
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(brush)
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(brush)
                )
            }

            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(50.dp)
                        .align(Alignment.CenterEnd)
                        .clip(RoundedCornerShape(10.dp))
                        .background(brush)
                )
            }

        }
    }
}

@Composable
@Preview(showBackground = true)
fun ShimmerGridItemPreview() {
    QuizShimmerGridItem(
        brush = Brush.linearGradient(
            listOf(
                Color.LightGray.copy(alpha = 0.6f),
                Color.LightGray.copy(alpha = 0.2f),
                Color.LightGray.copy(alpha = 0.6f),
            )
        )
    )
}

@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun ShimmerGridItemDarkPreview() {
    QuizShimmerGridItem(
        brush = Brush.linearGradient(
            listOf(
                Color.LightGray.copy(alpha = 0.6f),
                Color.LightGray.copy(alpha = 0.2f),
                Color.LightGray.copy(alpha = 0.6f),
            )
        )
    )
}