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

        val mainActivity = Intent(this, MainActivity::class.java)
        val authActivity = Intent(this, AuthActivity::class.java)

        firebaseAuth.addAuthStateListener {
            if(it.currentUser == null){
                startActivity(authActivity)
            }
            else{
                startActivity(mainActivity)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        finish()
    }
}