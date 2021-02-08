package com.example.noter.ui.main

import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.noter.R


class NotesFragment : Fragment() {
    private var toolbar: Toolbar? = null
    private var toolbarHead: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notes, container, false)


        toolbar = activity?.findViewById(R.id.my_toolbar)
        toolbarHead = activity?.findViewById(R.id.toolbar_head_edit)

        toolbar?.title = null
        toolbarHead?.isFocusable = false

        toolbarHead?.setText(R.string.menu_search)
        val toolbarHeadLayout = toolbarHead?.layoutParams
        toolbarHeadLayout?.width = ViewGroup.LayoutParams.MATCH_PARENT
        toolbarHead?.layoutParams = toolbarHeadLayout

        //  Open the Search Fragment
        toolbarHead?.setOnClickListener {
            val fragment = SearchFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.content_fragment, fragment)
            transaction?.addToBackStack(null)
            transaction?.commit()
        }

        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.menu_search).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val toolbarHeadLayout = toolbarHead?.layoutParams
        toolbarHeadLayout?.width = 0
        toolbarHead?.layoutParams = toolbarHeadLayout
    }
}