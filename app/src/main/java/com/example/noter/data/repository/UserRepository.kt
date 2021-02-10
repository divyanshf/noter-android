package com.example.noter.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepository
@Inject
constructor(
    private var firebaseFirestore: FirebaseFirestore,
    private var firebaseAuth: FirebaseAuth
){
    fun getUser() = flow<FirebaseUser?> {
        emit(firebaseAuth.currentUser)
    }

    fun loginUser(email:String, password:String){
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Log.i("LOGIN", "YES")
                }else{
                    Log.i("LOGIN", "${it.exception}")
                }
            }
    }

    fun registerUser(email:String, password:String){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        Log.i("REGISTER", "YES")
                    }else{
                        Log.i("REGISTER", "${it.exception}")
                    }
                }
    }
}