package com.samedtemiz.sigmawords.data.model

data class Question(
    val id: String,                 // Kelimenin benzersiz id'si
    val questionTerm: String,       // Sorulan kelime
    val options: List<String>,      // Tüm şıklar
//    val correctOptionIndex: Int     // Doğru şıkkın dizini
)