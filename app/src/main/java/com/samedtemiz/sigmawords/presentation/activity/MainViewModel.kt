package com.samedtemiz.sigmawords.presentation.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.samedtemiz.sigmawords.di.retrofit.RetrofitRepository
import com.samedtemiz.sigmawords.domain.models.Word
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: RetrofitRepository) : ViewModel() {

    private var _wordsList: MutableLiveData<Word> = MutableLiveData()
    val wordsList: LiveData<Word> get() = _wordsList

    init {
        getObserverLiveData()
        loadData()
    }

    fun getObserverLiveData(): MutableLiveData<Word> {
        return _wordsList
    }

    fun loadData() {
        repository.getWords_A1(_wordsList)
    }

}