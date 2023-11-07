package com.samedtemiz.sigmawords.data.model

data class Word(
    val category: String? = "",
    val id: String? = "",
    val meaning: String? = "",
    val meaning2: String? = "",
    val meaning3: String? = "",
    var shownDate: String? = "",
    var sigmaDate: String? = "",
    var sigmaLevel: Int? = 0,
    var success: Int? = 0,
    val term: String? = "",
)