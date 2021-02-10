package com.example.noter.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.noter.data.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel
@Inject
constructor(
    private var userRepository: UserRepository
): ViewModel() {
    private var user = userRepository.getUser().asLiveData()

    fun getUser() : LiveData<FirebaseUser?> {
        return user
    }

    fun login(email:String, password:String){
        viewModelScope.launch {
            userRepository.loginUser(email, password)
        }
    }

    fun register(email:String, password: String){
        viewModelScope.launch {
            userRepository.registerUser(email, password)
        }
    }
}