package com.example.noter.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.noter.R
import com.example.noter.ui.auth.AuthActivity
import com.example.noter.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EmptyActivity : AppCompatActivity() {
    @Inject
    lateinit var firebaseAuth:FirebaseAuth
    private var theme:String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty)

        val mainActivity = Intent(this, MainActivity::class.java)
        val authActivity = Intent(this, AuthActivity::class.java)
        val sp = this.getSharedPreferences("com.example.noter_preferences", 0)

        theme = sp.getString("theme_preference", "system")
        Log.i("Theme Empty", "$theme")
        setTheme(theme)

        firebaseAuth.addAuthStateListener {
            if(it.currentUser == null){
                startActivity(authActivity)
            }
            else{
                startActivity(mainActivity)
            }
        }
    }

    private fun setTheme(theme:String?){
        when(theme){
            "system" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            "dark" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            "light" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            else -> {
                Log.i("Theme Empty", "Why god why?")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        finish()
    }
}