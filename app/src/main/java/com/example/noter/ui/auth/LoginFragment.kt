package com.example.noter.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.noter.R
import com.example.noter.data.viewmodel.UserViewModel
import com.example.noter.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val userViewModel: UserViewModel by viewModels()
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var warningTextView: TextView
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var navController:NavController
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
        warningTextView = view.findViewById(R.id.login_warning)

        view.findViewById<Button>(R.id.login_button).setOnClickListener {
            warningTextView.visibility = View.INVISIBLE
            if(validate()){
                try {
                    userViewModel.login(email, password)
                }
                catch (e:Exception){
                    e.printStackTrace()
                }
                checkUser()
            }
            else{
                warningTextView.visibility = View.VISIBLE
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    private fun checkUser(){
        firebaseAuth.addAuthStateListener {
            if(it.currentUser != null){
                try {
                    val intent = Intent(activity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
            else{
                warningTextView.visibility = View.VISIBLE
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
        if(email.isNotEmpty() and password.isNotEmpty()){
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
        return false
    }

}