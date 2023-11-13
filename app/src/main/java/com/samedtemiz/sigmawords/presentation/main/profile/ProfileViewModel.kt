package com.samedtemiz.sigmawords.presentation.main.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samedtemiz.sigmawords.data.repository.user.UserRepository
import com.samedtemiz.sigmawords.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "ProfileViewModel"

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val stats: MutableLiveData<UiState<Pair<String, String>>> = MutableLiveData()

    init {
        stats.value = UiState.Loading
    }

    fun calculateStats(){

    }
}