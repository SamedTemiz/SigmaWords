package com.samedtemiz.sigmawords.data.model

data class Quiz(
    val quizId: String? = "",
    val questions: List<Question>? = listOf(),
    val date: String? = "",
    var result: Result? = null,
    var solved: Boolean? = false
)

