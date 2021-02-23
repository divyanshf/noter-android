package com.example.noter.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.noter.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment(), View.OnClickListener {
    private var navController:NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.login_button).setOnClickListener(this)
        view.findViewById<Button>(R.id.register_button).setOnClickListener(this)
    }

    override fun onClick(view:View){
        when(view.id){
            R.id.login_button -> navController!!.navigate(R.id.action_authFragment_to_loginFragment)
            R.id.register_button -> navController!!.navigate(R.id.action_authFragment_to_registerFragment)
        }
    }

}