package com.samedtemiz.sigmawords.presentation.main.quiz

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.samedtemiz.sigmawords.data.DataStoreRepository
import com.samedtemiz.sigmawords.data.model.Question
import com.samedtemiz.sigmawords.data.model.Word
import com.samedtemiz.sigmawords.data.repository.user.UserRepository
import com.samedtemiz.sigmawords.data.repository.word.WordRepository
import com.samedtemiz.sigmawords.util.Constant.DATE_FORMATTER
import com.samedtemiz.sigmawords.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.random.Random


@HiltViewModel
class QuizViewModel @Inject constructor(
    private val wordrepository: WordRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val TAG = "QuizViewModel"

    private val _words = MutableLiveData<List<Word>>()
    val words: LiveData<List<Word>> = _words

    private val _quiz = MutableLiveData<UiState<List<Question>>>()
    val quiz: LiveData<UiState<List<Question>>> = _quiz

    private val _isSolved = MutableLiveData<Boolean>()
    val isSolved: LiveData<Boolean> = _isSolved

    init {
        // Kelimeler ve Kullanıcı verisi FireStore üzerinden geliyor
        loadData()

        userRepository.dailyQuizStatus(
            userId = Firebase.auth.uid.toString(),
            currentDate = LocalDateTime.now().format(DATE_FORMATTER),
            isSolved = _isSolved
        )

        if(_isSolved.value == false){
            prepareQuiz(10)
        }
    }

    private fun prepareQuiz(numberOfQuestions: Int) {
        _quiz.value = UiState.Loading

        words.observeForever { wordList ->
            val questions = createQuestionsFromWords(wordList, numberOfQuestions)
            // Oluşturulan soru listesini LiveData'ya at
            _quiz.value = questions
        }
    }
    private fun createQuestionsFromWords(words: List<Word>, numberOfQuestions: Int): UiState<List<Question>> {
        val questions = mutableListOf<Question>()

        repeat(numberOfQuestions) {
            val selectedWord = words.random()
            val question = selectedWord.term
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

            val questionObject = Question(
                id = selectedWord.id ?: "",
                questionTerm = question ?: "",
                options = options
            )

            questions.add(questionObject)
        }

        return UiState.Success(questions)
    }
    private fun loadData() {
        wordrepository.getAllWords(
            result = _words,
            wordsListName = "AllWords"
        )
    }


    fun updateQuizStatus(isSolved: Boolean) {
        // İşlemler yapıldıktan sonra isSolved değeri true olarak güncellenir
        userRepository.updateQuizStatus(
            userId = Firebase.auth.uid.toString(),
            isSolved = isSolved,
            solvedDate = LocalDateTime.now().format(DATE_FORMATTER)
        )
    }
}