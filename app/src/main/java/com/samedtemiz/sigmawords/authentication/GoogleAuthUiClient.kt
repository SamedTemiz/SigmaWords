package com.samedtemiz.sigmawords.authentication

import android.R.attr.data
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.samedtemiz.sigmawords.R
import com.samedtemiz.sigmawords.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException


private const val TAG = "Google Auth Ui Client"

class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    private val auth = Firebase.auth
    private var googleIdToken: String? = ""

    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()

        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }

        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)

        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            SignInResult(
                data = user?.run {
                    User(
                        userId = uid,
                        username = displayName,
                        profilePictureUrl = photoUrl?.toString(),
                        email = email
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    suspend fun singOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): User? = auth.currentUser?.run {
        User(
            userId = uid,
            username = displayName,
            profilePictureUrl = photoUrl?.toString(),
            email = email,
        )
    }

    fun deleteUser() {
        val user = auth.currentUser

        // Kullanıcının oturumu açık mı kontrol et
        if (user != null) {
            // Kullanıcının Google ile giriş yaptığından emin olun
            if (user.providerData.any { it.providerId == GoogleAuthProvider.PROVIDER_ID }) {
                // Google Auth ID token'ını alın
                if (googleIdToken != null) {
                    // ID token'ı kullanarak Firebase Authentication'da doğrulama yapın
                    val credential = GoogleAuthProvider.getCredential(googleIdToken, null)

                    // Kullanıcıyı silme veya diğer işlemleri gerçekleştirin
                    CoroutineScope(Dispatchers.IO).launch {
                        user.reauthenticate(credential)
                            .addOnCompleteListener { reauthTask ->
                                if (reauthTask.isSuccessful) {
                                    // Kullanıcıyı sil
                                    user.delete()
                                        .addOnCompleteListener { deleteTask ->
                                            if (deleteTask.isSuccessful) {
                                                // Kullanıcı başarıyla silindi
                                                Log.d(TAG, "Kullanıcı başarıyla silindi.")
                                            } else {
                                                // Silme işlemi başarısız oldu
                                                Log.w(
                                                    TAG,
                                                    "Kullanıcı silinirken hata oluştu",
                                                    deleteTask.exception
                                                )
                                            }
                                        }
                                } else {
                                    // Giriş bilgileri doğrulama başarısız oldu
                                    Log.w(
                                        TAG,
                                        "Giriş bilgileri doğrulanırken hata oluştu",
                                        reauthTask.exception
                                    )
                                }
                            }
                    }
                } else {
                    // Google ID token'ı alınamadı
                    Log.w(TAG, "Google ID token'ını alırken hata oluştu.")
                }
            } else {
                // Kullanıcı Google ile giriş yapmamışsa
                Log.w(TAG, "Kullanıcı Google ile giriş yapmamış.")
            }
        } else {
            // Kullanıcı oturumu açık değilse
            Log.w(TAG, "Kullanıcı oturumu açık değil.")
        }

        Log.w(TAG, "BABBBAAA")
    }
}