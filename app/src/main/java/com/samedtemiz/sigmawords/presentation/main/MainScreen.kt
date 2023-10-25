package com.samedtemiz.sigmawords.presentation.main

import android.annotation.SuppressLint
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.samedtemiz.sigmawords.authentication.GoogleAuthUiClient
import com.samedtemiz.sigmawords.presentation.Screen
import com.samedtemiz.sigmawords.presentation.main.home.HomeScreen
import com.samedtemiz.sigmawords.presentation.main.profile.ProfileScreen
import com.samedtemiz.sigmawords.presentation.main.quiz.QuizScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    mainNavController: NavController,
    navController: NavHostController = rememberNavController(),
    googleAuthUiClient: GoogleAuthUiClient
) {
    val context = LocalContext.current
    val navigationBarItems = remember { NavigationBarItems.values() }
    var selectedIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        bottomBar = {
            AnimatedNavigationBar(
                selectedIndex = selectedIndex,
                modifier = Modifier.height(64.dp),
                ballAnimation = Parabolic(tween(300)),
                indentAnimation = Height(tween(300)),
                barColor = MaterialTheme.colorScheme.primary,
                ballColor = MaterialTheme.colorScheme.primary,

            ) {
                navigationBarItems.forEach { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .noRippleClickable {
                                selectedIndex = item.ordinal


                                navController.navigate(item.route)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(26.dp),
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (selectedIndex == item.ordinal) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.inversePrimary
                        )
                    }
                }
            }
        }
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            NavHost(
                navController = navController,
                route = Screen.Main.route,
                startDestination = Screen.Main.Home.route
            ) {
                composable(Screen.Main.Home.route) {
                    // Home Screen
                    HomeScreen()
                }

                composable(Screen.Main.Quiz.route) {
                    // Quiz Screen
                    QuizScreen()
                }

                composable(Screen.Main.Profile.route) {
                    // Profile Screen
                    val userData = remember { googleAuthUiClient.getSignedInUser() }

                    var signOutTrigger by remember { mutableStateOf(false) }

                    if (signOutTrigger) {
                        LaunchedEffect(true) {
                            // Coroutine içerisinde çalışacak işlemler
                            try {
                                googleAuthUiClient.singOut()
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "Sign out",
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

                    ProfileScreen(
                        userData = userData,
                        onSignOut = {
                            signOutTrigger = true
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

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }

    return viewModel(parentEntry)
}