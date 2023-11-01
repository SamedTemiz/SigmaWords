package com.samedtemiz.sigmawords.data.model

data class Question(
    val id: String? = "",                       // Kelimenin benzersiz id'si
    val questionTerm: String? = "",             // Sorulan kelime
    val options: List<String>? = listOf(),      // Tüm şıklar
    val correctOptionIndex: Int? = 0            // Doğru şıkkın dizini
)