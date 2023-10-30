package com.samedtemiz.sigmawords.presentation.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.google.android.gms.auth.api.identity.Identity
import com.samedtemiz.sigmawords.authentication.GoogleAuthUiClient
import com.samedtemiz.sigmawords.authentication.SignInViewModel
import com.samedtemiz.sigmawords.data.model.User
import com.samedtemiz.sigmawords.presentation.Screen
import com.samedtemiz.sigmawords.presentation.main.MainScreen
import com.samedtemiz.sigmawords.presentation.main.home.HomeViewModel
import com.samedtemiz.sigmawords.presentation.main.profile.ProfileScreen
import com.samedtemiz.sigmawords.presentation.sign_in.SignInScreen
import com.samedtemiz.sigmawords.presentation.welcome.SplashViewModel
import com.samedtemiz.sigmawords.presentation.ui.theme.SigmaWordsTheme
import com.samedtemiz.sigmawords.presentation.welcome.OnboardingScreen
import com.samedtemiz.sigmawords.presentation.welcome.SplashScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var splashViewModel: SplashViewModel

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private val viewModel by lazy {
        ViewModelProvider(this, defaultViewModelProviderFactory)[HomeViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SigmaWordsTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    !splashViewModel.isLoading.value

                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Welcome.route
                    ) {
                        // Welcome
                        navigation(
                            startDestination = Screen.Welcome.Splash.route,
                            route = Screen.Welcome.route
                        ) {
                            composable(Screen.Welcome.Splash.route) {
                                // Splash Screen
                                SplashScreen(
                                    navController = navController,
                                    splashViewModel = splashViewModel
                                )
                            }

                            composable(Screen.Welcome.OnBoard.route) {
                                // OnBoard Screen
                                OnboardingScreen(navController = navController)
                            }
                        }

                        // Sign in
                        composable(Screen.SignIn.route) {
                            val viewModel: SignInViewModel = hiltViewModel()
                            val state by viewModel.state.collectAsStateWithLifecycle()

                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = { result ->
                                    if (result.resultCode == RESULT_OK) {
                                        lifecycleScope.launch {
                                            val signInResult = googleAuthUiClient.signInWithIntent(
                                                intent = result.data ?: return@launch
                                            )

                                            viewModel.onSignInResult(signInResult)
                                        }
                                    }

                                }
                            )

                            LaunchedEffect(key1 = Unit) {
                                if (googleAuthUiClient.getSignedInUser() != null) {
                                    navController.navigate(Screen.Main.route)
                                }
                            }

                            LaunchedEffect(key1 = state.isSignInSuccessful) {
                                if (state.isSignInSuccessful) {
                                    viewModel.createUserDatabaseIfNotExist(googleAuthUiClient.getSignedInUser()!!)

                                    Toast.makeText(
                                        applicationContext,
                                        "Sign in successful",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    navController.navigate(Screen.Main.route)
                                    viewModel.resetState()
                                }
                            }

                            SignInScreen(
                                state = state,
                                onSignInClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthUiClient.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
                                    }
                                }
                            )
                        }

                        // Main
                        composable(Screen.Main.route) {
                            MainScreen(
                                mainNavController = navController,
                                googleAuthUiClient = googleAuthUiClient
                            )
                        }
                    }
                }

            }
        }
    }

}


