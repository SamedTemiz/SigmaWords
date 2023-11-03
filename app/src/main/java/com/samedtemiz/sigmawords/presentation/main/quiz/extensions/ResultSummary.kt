package com.samedtemiz.sigmawords.presentation.main.quiz.extensions

import com.samedtemiz.sigmawords.data.model.Question
import com.samedtemiz.sigmawords.data.model.Word

data class ResultSummary(
    val questionCount: Int,
    val correctCount: Int,
    val wrongAnswers: List<Word>
)

fun calculateResultSummary(questions: List<Question>): ResultSummary {
    val questionCount = questions.size
    val correctCount = questions.count { question ->
        question.selectedOptionIndex == question.correctOptionIndex
    }

    val wrongAnswers = questions
        .filter { question ->
            question.selectedOptionIndex != question.correctOptionIndex
        }
        .mapNotNull { question ->
            question.questionWord
        }

    return ResultSummary(
        questionCount = questionCount,
        correctCount = correctCount,
        wrongAnswers = wrongAnswers
    )
}

private fun createSigmaWordsList(words: List<Word>): Int {
    return words.size // Eğer listenin boyutunu almak istiyorsanız, listenin boyutunu döndürebilirsiniz.
}

