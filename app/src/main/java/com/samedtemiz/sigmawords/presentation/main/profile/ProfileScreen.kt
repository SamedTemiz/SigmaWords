package com.samedtemiz.sigmawords.presentation.main.profile

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.mukesh.MarkDown
import com.samedtemiz.sigmawords.data.model.User
import com.samedtemiz.sigmawords.presentation.main.home.formatDate
import com.samedtemiz.sigmawords.util.UiState
import com.samedtemiz.sigmawords.util.UnknownErrorComponent

private const val TAG = "Profile Screen"

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onSignOut: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    val userState by viewModel.user.observeAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.getUserData()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            userState?.let { state ->
                when (state) {
                    is UiState.Loading -> {
                        CircularProgressIndicator()
                    }

                    is UiState.Failure -> {
                        Log.d(TAG, state.error.toString())
                        UnknownErrorComponent("Profil bilgileri bulunamadı!")
                    }

                    is UiState.Success -> {
                        // Top Section
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(2f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ProfileResume(
                                user = state.data,
                                onSignOut = {  },
                                onDeleteAccount = { onDeleteAccount() },
                                viewModel
                            )
                        }

                        // Information
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Top
                        ) {
                            Text(
                                text = state.data.username ?: "NOT-FOUND",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = state.data.email ?: "NOT-FOUND",
                                fontSize = 20.sp,
                            )
                        }
                    }
                }

            }

            // Others
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3.5f)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        InfoCard(
                            painterID = InformationItems.SigmaNedir.icon,
                            title = InformationItems.SigmaNedir.title,
                            description = InformationItems.SigmaNedir.description
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        InfoCard(
                            painterID = InformationItems.Algoritma.icon,
                            title = InformationItems.Algoritma.title,
                            description = InformationItems.Algoritma.description
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        InfoCard(
                            painterID = InformationItems.Kaynaklar.icon,
                            title = InformationItems.Kaynaklar.title,
                            description = InformationItems.Kaynaklar.description
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        InfoCard(
                            painterID = InformationItems.Gelistirici.icon,
                            title = InformationItems.Gelistirici.title,
                            description = InformationItems.Gelistirici.description
                        )
                    }
                }
            }

            // For ui
            Box(modifier = Modifier.weight(0.5f))
        }
    }
}

@Composable
fun InfoCard(
    painterID: Int,
    title: String,
    description: String
) {
    var showDialog by remember { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(7.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        modifier = Modifier
            .fillMaxSize()
            .clickable { showDialog = true }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = painterID),
                contentDescription = "",
                modifier = Modifier.size(50.dp)
            )

            Text(
                text = title,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(title)
            },
            text = {
                SelectionContainer {
                    MarkDown(
                        text = description,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp)),
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("Tamam")
                }
            }
        )
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileResume(
    user: User,
    onSignOut: () -> Unit,
    onDeleteAccount: () -> Unit,
    viewModel: ProfileViewModel
) {
    GlideImage(
        model = user.profilePictureUrl,
        contentDescription = "Google Account Profile Picture",
        modifier = Modifier
            .fillMaxHeight(0.7f)
            .fillMaxWidth(0.4f)
            .border(1.dp, MaterialTheme.colorScheme.background, CircleShape)
            .clip(CircleShape)
            .background(Color.White),
        contentScale = ContentScale.Crop,
    )
    Column(
        Modifier
            .fillMaxHeight(0.7f)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Card(
            shape = RoundedCornerShape(7.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(30.dp)
                .clickable {
                    onSignOut()
                }
        ) {
            Text(
                text = "ÇIKIŞ",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 4.dp)
            )
        }
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Kayıt tarihi", fontSize = 16.sp)
            Text(
                text = user.registerDate?.formatDate().toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            var accountInfo by remember { mutableStateOf(false) }
            Card(
                shape = RoundedCornerShape(7.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(30.dp)
                    .clickable {
                        accountInfo = true
                    }
            ) {
                Text(
                    text = "Hesap",
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 4.dp)
                )
            }

            if (accountInfo) {
                var showResetDialog by remember { mutableStateOf(false) }
                var showDeleteDialog by remember { mutableStateOf(false) }
                val context = LocalContext.current

                AlertDialog(
                    onDismissRequest = { accountInfo = false },
                    title = {
                        Text("Hesap Ayarları")
                    },
                    text = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "Kayıt tarihi", fontSize = 16.sp)
                            Text(
                                text = user.registerDate?.formatDate().toString(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { showResetDialog = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = MaterialTheme.colorScheme.onError
                                )
                            ) {
                                Text(
                                    text = "İlerlemeyi sıfırla",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Button(
                                onClick = { showDeleteDialog = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                                )
                            ) {
                                Text(
                                    text = "Hesabı sil",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        if (showResetDialog) {
                            SimpleConfirmationDialog(
                                title = "İlerlemeyi Sıfırla",
                                message = "İlerlemeyi sıfırlamak istediğinizden emin misiniz?",
                                onConfirm = {
                                    if (viewModel.resetUserData()) {
                                        Toast.makeText(
                                            context,
                                            "İlerleme sıfırlandı",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }

                                    showResetDialog = false
                                    accountInfo = false
                                },
                                onDismiss = {
                                    showResetDialog = false
                                }
                            )
                        }

                        if (showDeleteDialog) {
                            SimpleConfirmationDialog(
                                title = "Hesabı Sil",
                                message = "Hesabı kalıcı olarak silmek istediğinizden emin misiniz?" +
                                        "\n Tüm ilerlemeniz de silinecektir.",
                                onConfirm = {
                                    viewModel.resetUserData()
                                    viewModel.deleteUserData()
                                    onDeleteAccount()

                                    showDeleteDialog = false
                                    accountInfo = false
                                },
                                onDismiss = {
                                    showDeleteDialog = false
                                }
                            )
                        }

                    },
                    confirmButton = {
                        Button(
                            onClick = { accountInfo = false }
                        ) {
                            Text("Tamam")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SimpleConfirmationDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onDismiss.invoke()
        },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = message)
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm.invoke()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                )
            ) {
                Text(text = "Evet")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss.invoke()
                }
            ) {
                Text(text = "Hayır")
            }
        }
    )
}