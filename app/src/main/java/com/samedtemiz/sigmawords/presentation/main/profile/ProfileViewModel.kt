package com.samedtemiz.sigmawords.presentation.main.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.samedtemiz.sigmawords.data.model.User
import com.samedtemiz.sigmawords.data.repository.user.UserRepository
import com.samedtemiz.sigmawords.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "ProfileViewModel"

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val user: MutableLiveData<UiState<User>> = MutableLiveData()

    init {
        user.value = UiState.Loading
    }

    fun getUserData() {
        userRepository.getUserDatabase(userId = Firebase.auth.uid.toString(), userData = user)
    }
}