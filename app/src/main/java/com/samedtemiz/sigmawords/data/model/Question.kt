package com.samedtemiz.sigmawords.data.model

data class Question(
    val id: String? = "",                       // Kelimenin benzersiz id'si
    val questionWord: Word? = null,             // Sorulan kelime
    val options: List<String>? = listOf(),      // Tüm şıklar
    val correctOptionIndex: Int? = 0,           // Doğru şıkkın dizini
    var selectedOptionIndex: Int? = null        // Seçilen şık indexi
)