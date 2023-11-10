package com.samedtemiz.sigmawords.presentation.main.quiz

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.samedtemiz.sigmawords.data.model.Question
import com.samedtemiz.sigmawords.data.model.Quiz
import com.samedtemiz.sigmawords.data.model.Word
import com.samedtemiz.sigmawords.data.repository.user.UserRepository
import com.samedtemiz.sigmawords.data.repository.word.WordRepository
import com.samedtemiz.sigmawords.util.Constant.CURRENT_DATE
import com.samedtemiz.sigmawords.util.UiState
import com.samedtemiz.sigmawords.data.model.Result
import com.samedtemiz.sigmawords.presentation.main.quiz.extensions.QuestionCreator
import com.samedtemiz.sigmawords.presentation.main.quiz.extensions.SigmaOperations
import com.samedtemiz.sigmawords.presentation.main.quiz.extensions.calculateResultSummary
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "QuizViewModel"
private val userId = Firebase.auth.uid.toString()

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val wordRepository: WordRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _words = MutableLiveData<List<Word>>()
    val words: LiveData<List<Word>> = _words

    private val _sigmaWords = MutableLiveData<List<Word>>()
    val sigmaWords: LiveData<List<Word>> = _sigmaWords

    private val _quiz = MutableLiveData<UiState<Quiz>>()
    val quiz: LiveData<UiState<Quiz>> = _quiz

    private val _result = MutableLiveData<UiState<Result>>()
    val result: LiveData<UiState<Result>> = _result

    init {
        _quiz.value = UiState.Loading

        try {
            userRepository.getQuiz(userId = userId, quiz = _quiz)
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
        }
    }

    // Quiz stuff
    fun createQuiz(questionCount: Int) {
        Log.d(TAG, "Yeni quiz oluşturuluyor.")
        _quiz.value = UiState.Loading
        
        val questions = createQuestions(questionCount = questionCount)
        questions.forEach {
            Log.d(TAG, it.id.toString())
        }

        val quiz = Quiz(
            quizId = "",
            questions = questions,
            date = CURRENT_DATE,
            result = null,
            solved = false
        )

        try {
            userRepository.addQuiz(userId, quiz)
            Log.d(TAG, "Veriler işleniyor.")
            userRepository.getQuiz(userId, _quiz)
            Log.d(TAG, "Quiz oluşturuldu.")
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
        }
    }
    private fun createQuestions(questionCount: Int): List<Question> {
        var regularQuestions: List<Question> = emptyList()
        var sigmaQuestions: List<Question> = emptyList()

        if (_words.value != null && _sigmaWords.value != null) {
            val questionCreator = QuestionCreator(_words.value!!)

            regularQuestions = questionCreator.createRegularQuestions(questionCount)
            sigmaQuestions = questionCreator.createSigmaQuestions(_sigmaWords.value!!)
        }

        return regularQuestions + sigmaQuestions
    }

    // Result stuff
    fun createResult(data: Quiz) {
        val quizId = data.quizId
        val resultDate = data.date
        val resultSummary = calculateResultSummary(data.questions ?: emptyList())

        val result = Result(
            quizId = quizId,
            resultDate = resultDate,
            questionCount = resultSummary.questionCount,
            correctCount = resultSummary.correctCount,
            wrongAnswers = resultSummary.wrongAnswers,
            correctAnswers = resultSummary.correctAnswers
        )

        data.result = result
        data.solved = true
        Log.d(TAG, "Result oluşturuluyor.")

        try {
            userRepository.updateQuiz(userId = userId, quiz = data)
            userRepository.addResult(userId = userId, result = result)

            addSigmaWords(resultSummary.correctAnswers)
            deleteSigmaWords(resultSummary.wrongAnswers)
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
        }

        Log.d(TAG, "Quiz solved durumu değiştirildi.")
    }
    fun getCurrentResult(quizId: String) {
        userRepository.getCurrentResult(userId = userId, quizId = quizId, _result)
    }

    // Sigma stuff
    private fun addSigmaWords(correctAnswers: List<Word>) {
        val sigmaOperations = SigmaOperations()
        val sigmaWords = sigmaOperations.calculateSigma(correctAnswers)

        try {
            userRepository.addSigmaWords(userId = userId, sigmaWords = sigmaWords)
            Log.d(TAG, "Sigma kelimeleri eklendi.")
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
        }
    }
    private fun deleteSigmaWords(wrongAnswers: List<Word>) {
        try {
            userRepository.deleteSigmaWords(userId = userId, forDeleteSigmaWords = wrongAnswers)
            Log.d(TAG, "Sigma kelimeleri silindi.")
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
        }
    }

    // Words stuff
    fun fetchWords(wordsListName: String) {
        Log.d(TAG, "Words fetch çalıştı.")
        wordRepository.getWords(_words, wordsListName)
        Log.d(TAG, "Sigma words fetch çalıştı.")
        userRepository.getUserSigmaWords(userId = userId, currentDate = CURRENT_DATE, _sigmaWords)
    }
}
