package com.samedtemiz.sigmawords.data.model

data class Result(
    val quizId: String? = "",
    val correctCount: Int? = 0,
    val wrongCount: Int? = 0,
    val sigmaWordCount: Int? = 0
)