package com.example.noter.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.noter.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel
@Inject
constructor(
    private var userRepository: UserRepository
): ViewModel() {
    var user = userRepository.getUser().asLiveData()

    fun getUser(){
        viewModelScope.launch {
            user = userRepository.getUser().asLiveData()
        }
    }

    fun login(email:String, password:String){
        viewModelScope.launch {
            userRepository.loginUser(email, password)
        }
    }

    fun register(name:String, email:String, password: String){
        viewModelScope.launch {
            userRepository.registerUser(name, email, password)
        }
    }
}