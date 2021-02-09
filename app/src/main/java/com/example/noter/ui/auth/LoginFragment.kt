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
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.example.noter.R
import com.example.noter.data.repository.UserRepository
import com.example.noter.data.viewmodel.UserViewModel
import com.example.noter.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var warningTextView: TextView
    private var email = ""
    private var password = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        emailEditText = view.findViewById(R.id.email_edit_text)
        passwordEditText = view.findViewById(R.id.password_edit_text)

        view.findViewById<Button>(R.id.login_button).setOnClickListener {
            if(validate()){
                userViewModel.login(email, password)
                checkUser()
            }
        }

        return view
    }

    private fun checkUser(){
        userViewModel.getUser().observe(viewLifecycleOwner, {
            if(it != null){
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            }
        })
    }

    private fun removeSpacing():String{
        var email = emailEditText.text.toString()
        return email.filter {
            it != ' '
        }
    }

    private fun validate():Boolean{
        email = removeSpacing()
        password = passwordEditText.text.toString()
        if(email.isNotEmpty() and password.isNotEmpty()){
            return true
        }
        return false
    }

}