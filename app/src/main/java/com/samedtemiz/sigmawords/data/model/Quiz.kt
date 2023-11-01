package com.samedtemiz.sigmawords.data.model

import com.samedtemiz.sigmawords.util.Constant.CURRENT_DATE
import java.time.LocalDateTime

data class Quiz(
    val questions: List<Question>? = listOf(),
    val date: String? = "",
    val result: Result? = null,
    val solved: Boolean? = false
)

