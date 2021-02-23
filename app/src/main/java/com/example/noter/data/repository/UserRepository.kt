package com.example.noter.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository
@Inject
constructor(
        private val firebaseAuth: FirebaseAuth
){
    fun getUser() = flow {
        emit(firebaseAuth.currentUser)
    }

    suspend fun loginUser(email:String, password:String) : Boolean{
        var result = false
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        result = true
                    }
                }
                .await()
        }catch (e:Exception){
            e.printStackTrace()
        }
        return result
    }

    suspend fun registerUser(name:String, email:String, password:String): Boolean {
        var result = false
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        result = true
                    }
                }
                .await()

            val profileUpdate = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

            firebaseAuth.currentUser!!
                .updateProfile(profileUpdate)
                .await()

        }catch (e:Exception){
            e.printStackTrace()
        }

        return result
    }
}