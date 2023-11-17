package com.samedtemiz.sigmawords.presentation.activity

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.google.android.gms.auth.api.identity.Identity
import com.samedtemiz.sigmawords.authentication.GoogleAuthUiClient
import com.samedtemiz.sigmawords.authentication.SignInViewModel
import com.samedtemiz.sigmawords.presentation.Screen
import com.samedtemiz.sigmawords.presentation.main.MainScreen
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
                                Log.d(TAG, "Splash screen")
                            }

                            composable(Screen.Welcome.OnBoard.route) {
                                // OnBoard Screen
                                OnboardingScreen(navController = navController)
                                Log.d(TAG, "OnBoarding screen")
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
                                    navController.popBackStack()
                                    navController.navigate(Screen.Main.route)
                                }
                            }

                            LaunchedEffect(key1 = state.isSignInSuccessful) {
                                if (state.isSignInSuccessful) {
                                    viewModel.createUserDatabaseIfNotExist(googleAuthUiClient.getSignedInUser()!!)
                                    Log.d(TAG, "Giriş başarılı.")

                                    navController.popBackStack()
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

                            Log.d(TAG, "Sign in screen")
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


