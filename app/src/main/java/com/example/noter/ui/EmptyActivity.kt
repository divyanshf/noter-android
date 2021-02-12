package com.example.noter.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.example.noter.R
import com.example.noter.data.viewmodel.UserViewModel
import com.example.noter.ui.auth.AuthActivity
import com.example.noter.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EmptyActivity : AppCompatActivity() {
    @Inject
    lateinit var firebaseAuth:FirebaseAuth
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty)

        var intent = Intent(this, MainActivity::class.java)

        firebaseAuth.addAuthStateListener {
            Log.i("User", it.currentUser.toString())
            if(it.currentUser == null){
                intent = Intent(this, AuthActivity::class.java)
            }
            startActivity(intent)
        }

    }
}