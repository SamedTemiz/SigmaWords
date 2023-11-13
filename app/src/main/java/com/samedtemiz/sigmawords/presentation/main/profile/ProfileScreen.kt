package com.samedtemiz.sigmawords.presentation.main.profile

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.samedtemiz.sigmawords.R
import com.samedtemiz.sigmawords.data.model.User
import com.samedtemiz.sigmawords.presentation.ui.theme.SigmaWordsTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileScreen(
    userData: User?,
    onSignOut: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Top Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlideImage(
                    model = userData?.profilePictureUrl,
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
                    Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "6",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = "Created Events",
                                fontSize = 16.sp,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "17",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = "Attended Events",
                                fontSize = 16.sp,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
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
                    text = userData?.username ?: "NOT-FOUND",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = userData?.email ?: "NOT-FOUND",
                    fontSize = 20.sp,
                )
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
                            title = "Sigma metodolojisi nedir?",
                            painterID = R.drawable.sigma_fill
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        InfoCard(
                            title = "Uygulama algoritması",
                            painterID = R.drawable.connection
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
                            title = "Kaynaklar",
                            painterID = R.drawable.document
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        InfoCard(
                            title = "Geliştirici hakkında",
                            painterID = R.drawable.about
                        )
                    }
                }
            }

            Box(modifier = Modifier.weight(0.5f))
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    painterID: Int
) {
    Card(
        shape = RoundedCornerShape(7.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        modifier = Modifier.fillMaxSize()
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
}