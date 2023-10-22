package com.samedtemiz.sigmawords.data.model

data class Word(
    val category: String? = "",
    val id: String? = "",
    val meaning: String? = "",
    val meaning2: String? = "",
    val meaning3: String? = "",
    val shownDate: String? = "",
    val sigmaDate: String? = "",
    val sigmaLevel: Int? = 0,
    val success: Int? = 0,
    val term: String? = ""
)