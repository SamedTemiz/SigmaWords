package com.samedtemiz.sigmawords.data.model

import android.net.Uri

data class User(
    val userId: String? = "",
    val username: String? = "",
    val profilePictureUrl: String? = "",
    val email: String? = "",
    var registerDate: String? = "",
)
