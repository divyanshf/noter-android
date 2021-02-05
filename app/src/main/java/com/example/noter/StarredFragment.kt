package com.example.noter

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText

class StarredFragment : Fragment() {
    private var toolbarHead: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_starred, container, false)

        toolbarHead = activity?.findViewById(R.id.toolbar_head)
        toolbarHead?.setText(R.string.starred)
        toolbarHead?.setOnClickListener(null)

        return view
    }
}