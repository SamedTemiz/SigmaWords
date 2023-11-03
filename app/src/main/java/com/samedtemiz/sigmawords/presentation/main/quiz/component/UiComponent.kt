package com.samedtemiz.sigmawords.presentation.main.quiz.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomElevationButton(
    onClick: () -> Unit,
    isEnabled: Boolean,
    elevation: Dp = 10.dp,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        enabled = isEnabled,
        elevation = ButtonDefaults.elevatedButtonElevation(elevation),
        modifier = modifier,
        content = content
    )
}
