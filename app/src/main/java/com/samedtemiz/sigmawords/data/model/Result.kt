package com.samedtemiz.sigmawords.data.model

data class Result(
    val quizId: String? = "",       // Çözülen quiz id'si
    val questionCount: Int? = 0,    // Quiz total soru sayısı
    val correctCount: Int? = 0,     // Quiz doğru cevaplanan soru sayısı
    val sigmaWordCount: Int? = 0,   // Sigma etiketine sahip sorular
    val wrongAnswers: List<Word>? = listOf(), // Yanlış cevaplanan kelimeler
)