package com.example.noter.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
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
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        result = true
                        Log.i("LOGIN", "YES")
                    }else{
                        Log.i("LOGIN", "${it.exception}")
                    }
                }
                .await()
        return result
    }

    suspend fun registerUser(email:String, password:String): Boolean {
        var result = false
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        result = true
                        Log.i("REGISTER", "YES")
                    }else{
                        Log.i("REGISTER", "${it.exception}")
                    }
                }
                .await()
        return result
    }
}