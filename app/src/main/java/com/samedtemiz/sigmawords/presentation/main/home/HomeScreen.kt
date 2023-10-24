package com.samedtemiz.sigmawords.presentation.main.home

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.samedtemiz.sigmawords.presentation.ui.theme.SigmaWordsTheme
import com.samedtemiz.sigmawords.util.UiState

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onSignOut: () -> Unit,
    onProfileScreen: () -> Unit
) {

    val wordList = viewModel.words.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row {
            Button(onClick = onSignOut) {
                Text(text = "Sign out")
            }
            Button(onClick = onProfileScreen) {
                Text(text = "Profile")
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            wordList.value?.run {
                when (this) {
                    is UiState.Loading -> {
                        CircularProgressIndicator()
                        Log.d("HomeScreen", "Loading")
                    }

                    is UiState.Failure -> {
                        Text(text = "HOME SCREEN", color = Color.Black)
                        Log.d("HomeScreen", error.toString())
                    }

                    is UiState.Success -> {
                        LazyColumn() {
                            items(data) { word ->
                                Column {
                                    word.term?.let {
                                        Text(
                                            text = it,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp
                                        )
                                    }

                                    word.meaning?.let {
                                        Text(text = it)
                                    }
                                    word.meaning2?.let {
                                        Text(text = it)
                                    }
                                    word.meaning3?.let {
                                        Text(text = it)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(10.dp))
        }
    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun HomeScreenPreview(){
//    SigmaWordsTheme {
//        HomeScreen(
//            homeViewModel = ,
//            onSignOut = {},
//            onProfileScreen = {}
//        )
//    }
//}