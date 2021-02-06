package com.example.noter.ui

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.noter.R


class NotesFragment : Fragment() {
    private var toolbarHead: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notes, container, false)

        toolbarHead = activity?.findViewById(R.id.toolbar_head)
        toolbarHead?.setText(R.string.menu_search)

        //  Add the search notes click listener
        toolbarHead?.setOnClickListener {
            Toast.makeText(activity, "Search", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}