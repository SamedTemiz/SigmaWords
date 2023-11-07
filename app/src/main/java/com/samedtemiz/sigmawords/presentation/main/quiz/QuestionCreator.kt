package com.samedtemiz.sigmawords.presentation.main.quiz

import android.util.Log
import com.samedtemiz.sigmawords.data.model.Question
import com.samedtemiz.sigmawords.data.model.Word
import com.samedtemiz.sigmawords.util.Constant.CURRENT_DATE
import com.samedtemiz.sigmawords.util.Options

private const val TAG = "Question Creator"
class QuestionCreator(private val wordsList: List<Word>, private val sigmaList: List<Word>) {
    private val questions = mutableListOf<Question>()
    private val selectedWords = mutableListOf<Word>()

    fun createQuestions(questionCount: Int): List<Question> {
        createRegularQuestions(wordsList, questionCount)
        createSigmaQuestions(sigmaList)
        return questions
    }

    private fun createRegularQuestions(wordList: List<Word>, questionCount: Int) {
        repeat(questionCount) {
            val selectedWord = wordList.filter { it.shownDate == "null" }.randomOrNull()

            if (selectedWord != null && selectedWord !in selectedWords) {
                selectedWord.shownDate = CURRENT_DATE
                selectedWords.add(selectedWord)

                val options = mutableListOf<String>().apply { add(selectedWord.meaning ?: "") }
                fillOptions(options, wordList)

                val questionObject = createQuestion(selectedWord, options)
                questions.add(questionObject)
            }
        }
    }

    private fun createSigmaQuestions(sigmaList: List<Word>) {
        sigmaList.forEach { sigmaWord ->
            val sigmaOptions = mutableListOf<String>().apply { add(sigmaWord.meaning ?: "") }
            wordsList.let { wordList ->
                fillSigmaOptions(sigmaOptions, sigmaList, wordList)
            }

            val sigmaQuestionObject = createQuestion(sigmaWord, sigmaOptions)
            questions.add(sigmaQuestionObject)
        }
    }

    private fun fillOptions(options: MutableList<String>, wordList: List<Word>) {
        while (options.size < (Options.OPTIONS_COUNT.get as? Int ?: 0)) {
            val randomOption = wordList.random().meaning
            if (randomOption !in options) {
                options.add(randomOption ?: "")
            }
        }
        options.shuffle()
    }

    private fun fillSigmaOptions(
        sigmaOptions: MutableList<String>,
        sigmaList: List<Word>,
        wordList: List<Word>
    ) {
        while (sigmaOptions.size < (Options.OPTIONS_COUNT.get as? Int ?: 0)) {
            val randomSigmaOption =
                wordList.filter { e -> !sigmaList.contains(e) }.random().meaning
            if (randomSigmaOption !in sigmaOptions) {
                sigmaOptions.add(randomSigmaOption ?: "")
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

