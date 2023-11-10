package com.samedtemiz.sigmawords.presentation.main.quiz.extensions

import com.samedtemiz.sigmawords.data.model.Word
import com.samedtemiz.sigmawords.util.Constant
import java.time.LocalDate

class SigmaOperations {

    fun calculateSigma(words: List<Word>): List<Word> {
        words.forEach{ word ->
            val date = LocalDate.parse(word.shownDate)              // Çözülen günün tarihi
            val (daysToAdd, newLevel) = when (word.sigmaLevel) {
                0 -> 1 to 1
                1 -> 3 to 2
                2 -> 7 to 3
                3 -> 15 to 4
                4 -> 21 to 5
                5 -> 30 to 6
                else -> throw IllegalArgumentException("Invalid sigma level")
            }

            val sigmaDate = date.plusDays(daysToAdd.toLong())
            word.sigmaDate = sigmaDate.format(Constant.DATE_FORMATTER)

            word.sigmaLevel = newLevel
            word.success = if(word.sigmaLevel == 6) 1 else 0
        }

        return words
    }

    fun resetSigma(words: List<Word>): List<Word>{
        words.forEach{ word->
            word.shownDate = "null"
            word.sigmaDate = "null"
            word.sigmaLevel = 0
            word.success = 0
        }

        return words
    }

}