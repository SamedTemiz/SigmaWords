package com.samedtemiz.sigmawords.presentation.main.quiz.question

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samedtemiz.sigmawords.R
import com.samedtemiz.sigmawords.presentation.ui.theme.SigmaWordsTheme

@Composable
fun QuestionScreen(
    question: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = question,
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.acherus_grotesque)),
            modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth()
        )
        options.forEach { option ->
            OptionItem(
                option = option,
                onOptionSelected = { onOptionSelected(it) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionItem(option: String, onOptionSelected: (String) -> Unit) {
    Card(
        shape= CardDefaults.outlinedShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSecondaryContainer),
        modifier = Modifier.fillMaxWidth(),
        onClick = { onOptionSelected(option) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = option,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview
@Composable
fun QuestionScreenPreview() {
    SigmaWordsTheme {
        QuestionScreen(
            question = "DENEME SORUSU",
            options = listOf("A", "B", "C", "D"),
            onOptionSelected = {})
    }
}
