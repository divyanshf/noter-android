package com.example.noter.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.example.noter.R
import com.example.noter.data.viewmodel.UserViewModel
import com.example.noter.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val userViewModel:UserViewModel by viewModels()
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var cnfPasswordEditText: EditText
    private var email = ""
    private var password = ""
    private var cnfPassword = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        emailEditText = view.findViewById(R.id.email_edit_text)
        passwordEditText = view.findViewById(R.id.password_edit_text)
        cnfPasswordEditText = view.findViewById(R.id.cnf_password_edit_text)

        view.findViewById<Button>(R.id.register_button).setOnClickListener {
            if(validate()){
                userViewModel.register(email, password)
                checkUser()
            }
        }

        return view
    }

    private fun checkUser(){
        firebaseAuth.addAuthStateListener {
            if(it.currentUser != null){
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun removeSpacing():String{
        val email = emailEditText.text.toString()
        return email.filter {
            it != ' '
        }
    }

    private fun validate():Boolean{
        email = removeSpacing()
        password = passwordEditText.text.toString()
        cnfPassword = cnfPasswordEditText.text.toString()
        if(email.isNotEmpty() and password.isNotEmpty()){
            if(password == cnfPassword && password.length >= 6){
                return true
            }
        }
        return false
    }
}