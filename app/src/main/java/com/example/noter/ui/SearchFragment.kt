package com.example.noter.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import com.example.noter.R
class SearchFragment : Fragment() {
    private var toolbar: Toolbar? = null
    private var toolbarHead: EditText? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        toolbar = activity?.findViewById(R.id.my_toolbar)
        toolbarHead = activity?.findViewById(R.id.toolbar_head)
        toolbarHead?.setText(R.string.menu_search)

        return view
    }

}