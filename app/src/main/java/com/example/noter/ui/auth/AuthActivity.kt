package com.example.noter.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.noter.R
import com.example.noter.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    private val username:String? = null  //  Temporary variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(username.isNullOrBlank()){
            setContentView(R.layout.activity_auth)
        }
        else{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}