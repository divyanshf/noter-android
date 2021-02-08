package com.example.noter.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import com.example.noter.R
import com.google.android.material.textfield.TextInputEditText

class StarredFragment : Fragment() {
    private var toolbarHead: EditText? = null
    private var toolbar: Toolbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_starred, container, false)

        toolbar = activity?.findViewById(R.id.my_toolbar)
//        toolbarHead = activity?.findViewById(R.id.toolbar_head_edit)
//        toolbarHead?.setText(R.string.starred)
//        toolbarHead?.setOnClickListener(null)
        toolbar?.setTitle(R.string.starred)

        return view
    }
}