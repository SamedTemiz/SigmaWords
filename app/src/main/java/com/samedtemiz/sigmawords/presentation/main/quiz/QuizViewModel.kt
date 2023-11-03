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
import dagger.hilt.android.lifecycle.HiltViewModel
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
                is UiState.Success -> {
                    if (quizValue.data.solved != null) {
                        _isSolved.value = quizValue.data.solved ?: false
                    } else {
                        getNewQuiz()
                    }

                    if (quizValue.data.solved == true) {
                        viewModelScope.launch {
                            userRepository.getResult(userId, quizValue.data.quizId!!, _result)
                        }
                    }
                }

                is UiState.Failure -> {
                    Log.d(TAG, quizValue.error.toString())
                    getNewQuiz()
                }

                else -> getNewQuiz()
            }
        }

        viewModelScope.launch {
            userRepository.getQuiz(userId, _quiz)
        }
    }

    private fun getNewQuiz() {
        val wordsListName = "A1"
        getWords(wordsListName)

        words.observeForever { wordList ->
            val questions = createQuestionsFromWords(wordList, 5)
            val quiz = Quiz(
                questions = questions,
                date = CURRENT_DATE,
                result = null,
                solved = false
            )

            // Eğer aynı tarihte quiz yoksa, oluşturulan quiz'i Success durumu içinde LiveData'ya atıyoruz
            _quiz.value = UiState.Success(quiz)
            _isSolved.value = false

            viewModelScope.launch{
                userRepository.addQuiz(userId, quiz)
            }
            Log.d(TAG, "Quiz oluşturuldu.")
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

                while (options.size < 4) {
                    val randomOption = words.random().meaning
                    if (randomOption !in options) {
                        options.add(randomOption ?: "")
                    }
                }

                options.shuffle()

                val shuffledCorrectOptionIndex = options.indexOf(correctAnswer)
                Log.d(TAG, shuffledCorrectOptionIndex.toString())

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

    private fun updateQuizStatus(quiz: Quiz) {
        // İşlemler yapıldıktan sonra quiz verisi güncelliyoruz
        viewModelScope.launch {
            userRepository.updateQuiz(
                userId = userId,
                quiz = quiz
            )
        }
        Log.d(TAG, "Quiz solved durumu değiştirildi: $isSolved")
    }

    fun createResult(data: Quiz) {
        val quizId = data.quizId
        val resultSummary =
            calculateResultSummary(data.questions ?: emptyList())

        val result = Result(
            quizId = quizId,
            questionCount = resultSummary.questionCount,
            correctCount = resultSummary.correctCount,
            wrongAnswers = resultSummary.wrongAnswers
        )

        viewModelScope.launch {
            data.result = result
            data.solved = true
            updateQuizStatus(data)
            _result.value = UiState.Success(result)

            userRepository.addResult(userId = userId, result = result)
        }
    }
}
