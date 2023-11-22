package com.samedtemiz.sigmawords.presentation.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.samedtemiz.sigmawords.authentication.GoogleAuthUiClient
import com.samedtemiz.sigmawords.data.model.User
import com.samedtemiz.sigmawords.presentation.Screen
import com.samedtemiz.sigmawords.presentation.activity.MainActivity
import com.samedtemiz.sigmawords.presentation.main.home.HomeScreen
import com.samedtemiz.sigmawords.presentation.main.home.HomeViewModel
import com.samedtemiz.sigmawords.presentation.main.profile.ProfileScreen
import com.samedtemiz.sigmawords.presentation.main.profile.ProfileViewModel
import com.samedtemiz.sigmawords.presentation.main.quiz.QuizScreen
import com.samedtemiz.sigmawords.presentation.main.quiz.QuizViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "Main Screen"

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    mainNavController: NavController,
    navController: NavHostController = rememberNavController(),
    googleAuthUiClient: GoogleAuthUiClient,
) {
    val context = LocalContext.current
    val user = remember { googleAuthUiClient.getSignedInUser() }

    val navigationBarItems = remember { NavigationBarItems.values() }
    var selectedIndex by remember { mutableIntStateOf(0) }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AnimatedNavigationBar(
                selectedIndex = selectedIndex,
                modifier = Modifier.height(64.dp),
                ballAnimation = Parabolic(tween(300)),
                indentAnimation = Height(tween(300)),
                barColor = MaterialTheme.colorScheme.background,
                ballColor = MaterialTheme.colorScheme.onBackground,

                ) {
                navigationBarItems.forEach { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .noRippleClickable {
                                selectedIndex = item.ordinal

                                navController.popBackStack()
                                navController.navigate(item.route)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (selectedIndex == item.ordinal) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.95f)
        ) {
            NavHost(
                navController = navController,
                route = Screen.Main.route,
                startDestination = Screen.Main.Home.route
            ) {
                composable(Screen.Main.Home.route) {
                    // Home Screen
                    selectedIndex = NavigationBarItems.Home.ordinal

                    val homeViewModel: HomeViewModel = hiltViewModel()
                    HomeScreen(viewModel = homeViewModel, userData = user)
                }

                composable(Screen.Main.Quiz.route) {
                    // Quiz Screen
                    selectedIndex = NavigationBarItems.Quiz.ordinal

                    val quizViewModel: QuizViewModel = hiltViewModel()
                    QuizScreen(viewModel = quizViewModel)
                }

                composable(Screen.Main.Profile.route) {
                    // Profile Screen
                    selectedIndex = NavigationBarItems.Profile.ordinal

                    var signOutTrigger by remember { mutableStateOf(false) }
                    if (signOutTrigger) {
                        LaunchedEffect(signOutTrigger) {
                            try {
                                googleAuthUiClient.singOut()
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "Çıkış başarılı",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                navController.popBackStack()
                                mainNavController.popBackStack()
                                mainNavController.navigate(Screen.Welcome.route)
                            } catch (e: Exception) {
                                // Hata yönetimi
                            }
                        }
                    }

                    var deleteAccount by remember { mutableStateOf(false) }
                    if (deleteAccount) {
                        LaunchedEffect(deleteAccount) {
                            try {
                                googleAuthUiClient.deleteUser()
                                googleAuthUiClient.singOut()
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "Hesap başarıyla silindi",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                                val intent = Intent(context, MainActivity::class.java)
                                // Eski activity'yi sonlandır
                                (context as? Activity)?.finish()
                                // Activity'yi tekrar başlat
                                context.startActivity(intent)

                            } catch (e: Exception) {
                                // Hata yönetimi
                            }
                        }
                    }


                    val viewModel: ProfileViewModel = hiltViewModel()
                    ProfileScreen(
                        viewModel = viewModel,
                        onSignOut = {
                            if (googleAuthUiClient.getSignedInUser() != null)
                                signOutTrigger = true
                            else Toast.makeText(
                                context,
                                "Hesap mevcut değil",
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        onDeleteAccount = {
                            if (googleAuthUiClient.getSignedInUser() != null)
                                deleteAccount = true
                            else Toast.makeText(
                                context,
                                "Hesap mevcut değil",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )
                }
            }
        }
    }
}

enum class NavigationBarItems(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    Home(
        route = Screen.Main.Home.route,
        title = "Home",
        icon = Icons.Default.Home
    ),
    Quiz(
        route = Screen.Main.Quiz.route,
        title = "Quiz",
        icon = Icons.Default.Done
    ),
    Profile(
        route = Screen.Main.Profile.route,
        title = "Profile",
        icon = Icons.Default.Person
    ),
}

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}