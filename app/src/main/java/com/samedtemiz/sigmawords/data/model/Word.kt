package com.samedtemiz.sigmawords.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class Word(
    @PrimaryKey val id: String = "",
    val category: String? = null,
    val meaning: String? = null,
    val meaning2: String? = null,
    val meaning3: String? = null,
    var shownDate: String? = null,
    var sigmaDate: String? = null,
    var sigmaLevel: Int = 0,
    var success: Int = 0,
    val term: String? = null,
)
