package com.samedtemiz.sigmawords.presentation.main.quiz.extensions

import android.util.Log
import com.samedtemiz.sigmawords.data.model.Question
import com.samedtemiz.sigmawords.data.model.Word
import com.samedtemiz.sigmawords.util.Constant
import com.samedtemiz.sigmawords.util.Options

private const val TAG = "Question Creator"
class QuestionCreator(private val wordsList: List<Word>) {

    private val questions = mutableListOf<Question>()
    private val selectedWords = mutableListOf<Word>()

    fun createRegularQuestions(questionCount: Int): List<Question> {
        Log.d(TAG, "Normal sorular oluşturuluyor.")
        repeat(questionCount) {
            val selectedWord = wordsList.filter { it.shownDate == "null" }.randomOrNull()

            if (selectedWord != null && selectedWord !in selectedWords) {
                selectedWord.shownDate = Constant.CURRENT_DATE
                selectedWords.add(selectedWord)

                val options = mutableListOf<String>().apply { add(selectedWord.meaning ?: "") }
                fillOptions(options, wordsList)

                val questionObject = createQuestion(selectedWord, options)
                questions.add(questionObject)
            }
        }

        return questions
    }

    fun createSigmaQuestions(sigmaList: List<Word>): List<Question> {
        Log.d(TAG, "Sigma sorular oluşturuluyor.")
        val sigmaQuestions = mutableListOf<Question>()

        sigmaList.forEach { sigmaWord ->
            val sigmaOptions = mutableListOf<String>().apply { add(sigmaWord.meaning ?: "") }
            wordsList.let { wordList ->
                fillSigmaOptions(sigmaOptions, sigmaList, wordList)
            }

            val sigmaQuestionObject = createQuestion(sigmaWord, sigmaOptions)
            sigmaQuestions.add(sigmaQuestionObject)
        }

        return sigmaQuestions
    }

    private fun fillOptions(options: MutableList<String>, wordList: List<Word>) {
        while (options.size < (Options.OPTIONS_COUNT.get as? Int ?: 0)) {
            val randomOption = wordList.random().meaning
            if (randomOption != null && randomOption !in options) {
                options.add(randomOption)
            }
        }
        options.shuffle()
    }

    private fun fillSigmaOptions(sigmaOptions: MutableList<String>, sigmaList: List<Word>, wordList: List<Word>) {
        while (sigmaOptions.size < (Options.OPTIONS_COUNT.get as? Int ?: 0)) {
            val randomSigmaOption =
                wordList.filter { e -> !sigmaList.contains(e) }.randomOrNull()?.meaning
            if (randomSigmaOption != null && randomSigmaOption !in sigmaOptions) {
                sigmaOptions.add(randomSigmaOption)
            }
        }
        sigmaOptions.shuffle()
    }

    private fun createQuestion(word: Word, options: List<String>): Question {
        val shuffledCorrectOptionIndex = options.indexOf(word.meaning)
        return Question(
            id = word.id ?: "",
            questionWord = word,
            options = options,
            correctOptionIndex = shuffledCorrectOptionIndex
        )
    }
}