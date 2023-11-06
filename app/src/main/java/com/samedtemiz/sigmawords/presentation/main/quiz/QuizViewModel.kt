package com.samedtemiz.sigmawords.presentation.main.quiz

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.samedtemiz.sigmawords.presentation.main.quiz.extensions.calculateResultSummary
import com.samedtemiz.sigmawords.util.Options
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

    private val _quiz = MutableLiveData<UiState<Quiz>>()
    val quiz: LiveData<UiState<Quiz>> = _quiz

    private val _isSolved = MutableLiveData<Boolean>()
    val isSolved: LiveData<Boolean> = _isSolved

    private val _result = MutableLiveData<UiState<Result>>()
    val result: LiveData<UiState<Result>> = _result

    init {
        _quiz.observeForever { quizValue ->
            when (quizValue) {
                is UiState.Loading -> {
                    Log.d(TAG, "Quiz verisi yükleniyor...")
                }

                is UiState.Failure -> {
                    Log.d(TAG, quizValue.error.toString())

                    getWords(Options.A1.get.toString())
                    createNewQuiz()
                }

                is UiState.Success -> {
                    _isSolved.postValue(quizValue.data.solved ?: false)
                    userRepository.getCurrentResult(
                        userId = userId,
                        quizId = quizValue.data.quizId ?: "",
                        result = _result
                    )

                    Log.d(TAG, "Quiz verisi alındı.")
                    Log.d(TAG, quizValue.data.toString())
                }
            }
        }

        viewModelScope.launch {
            userRepository.getQuiz(userId, _quiz)
        }
    }

    private fun createNewQuiz() {
        _words.observeForever { wordList ->
            if(wordList.isNotEmpty()){
                val questions = createQuestionsFromWords(wordList, Options.QUESTION_COUNT.get as Int)
                val quiz = Quiz(
                    quizId = "",
                    questions = questions,
                    date = CURRENT_DATE,
                    result = null,
                    solved = false
                )

                viewModelScope.launch {
                    userRepository.addQuiz(userId, quiz)
                    Log.d(TAG, "2 saniye delay uygulanıyor.")
                    delay(2000)
                    userRepository.getQuiz(userId, _quiz)
                }

                Log.d(TAG, "Quiz oluşturuldu.")
            }else{
                Log.d(TAG, "Kelime listesi boş.")
            }
        }
    }

    private fun createQuestionsFromWords(words: List<Word>, numberOfQuestions: Int): List<Question> {
        val questions = mutableListOf<Question>()
        val selectedWords = mutableListOf<Word>()

        repeat(numberOfQuestions) {
            val selectedWord = words.random()
            if (selectedWord !in selectedWords) {
                selectedWords.add(selectedWord)
                val correctAnswer = selectedWord.meaning

                val options = mutableListOf<String>()
                options.add(correctAnswer ?: "")

                while (options.size < Options.OPTIONS_COUNT.get as Int) {
                    val randomOption = words.random().meaning
                    if (randomOption !in options) {
                        options.add(randomOption ?: "")
                    }
                }

                options.shuffle()

                val shuffledCorrectOptionIndex = options.indexOf(correctAnswer)
                val questionObject = Question(
                    id = selectedWord.id ?: "",
                    questionWord = selectedWord,
                    options = options,
                    correctOptionIndex = shuffledCorrectOptionIndex
                )

                questions.add(questionObject)
            }
        }

        Log.d(TAG, "Sorular oluşturuldu.")
        return questions
    }

    private fun getWords(wordsListName: String) {
        viewModelScope.launch {
            wordRepository.getWords(
                result = _words,
                wordsListName = wordsListName
            )
        }

        Log.d(TAG, "Kelime listesi yüklendi")
    }

    fun createResult(data: Quiz) {
        val quizId = data.quizId
        val resultDate = data.date
        val resultSummary = calculateResultSummary(data.questions ?: emptyList())

        val result = Result(
            quizId = quizId,
            resultDate = resultDate,
            questionCount = resultSummary.questionCount,
            correctCount = resultSummary.correctCount,
            wrongAnswers = resultSummary.wrongAnswers
        )

        viewModelScope.launch {
            data.result = result
            data.solved = true

            _result.postValue(UiState.Success(result))
            Log.d(TAG, "Result oluşturuldu.")

            userRepository.updateQuiz(userId = userId, quiz = data)
            userRepository.addResult(userId = userId, result = result)

            Log.d(TAG, "Quiz solved durumu değiştirildi.")
        }
    }
}
