package com.example.noter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.noter.R

class ArchivesFragment : Fragment() {
    private var toolbarHead: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_archives, container, false)

        toolbarHead = activity?.findViewById(R.id.toolbar_head)
        toolbarHead?.setText(R.string.archives)
        toolbarHead?.setOnClickListener(null)

        return view
    }

}