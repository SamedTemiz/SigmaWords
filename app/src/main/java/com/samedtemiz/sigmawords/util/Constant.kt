package com.samedtemiz.sigmawords.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Constant{
    const val WORDS_BASE_URL = "https://raw.githubusercontent.com/SamedTemiz/sources/main/SigmaWords/"

    val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val CURRENT_DATE: String = LocalDateTime.now().format(DATE_FORMATTER)
}
