package com.example.noter.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.noter.R
import com.google.android.material.textfield.TextInputEditText

class ArchivesFragment : Fragment() {
    private var toolbarHead: EditText? = null
    private var toolbar:Toolbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_archives, container, false)

        toolbar = activity?.findViewById(R.id.my_toolbar)
//        toolbarHead = activity?.findViewById(R.id.toolbar_head_edit)
//        toolbarHead?.setText(R.string.archives)
//        toolbarHead?.setOnClickListener(null)
        toolbar?.setTitle(R.string.archives)

        return view
    }

}