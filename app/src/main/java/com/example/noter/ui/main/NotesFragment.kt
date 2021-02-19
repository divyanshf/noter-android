package com.example.noter.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noter.R
import com.example.noter.data.model.Note
import com.example.noter.data.viewmodel.NotesViewModel
import com.example.noter.ui.adapter.NotesAdapter
import com.example.noter.ui.edit.EditActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesFragment : Fragment(), NotesAdapter.OnItemClickListener{
    private var toolbar: Toolbar? = null
    private var toolbarHead: EditText? = null
    private var fabAdd: FloatingActionButton? = null
    private lateinit var recyclerView:RecyclerView
    private lateinit var recyclerViewAdapter:NotesAdapter
    private var notes:List<Note> = ArrayList()
    private val notesViewModel:NotesViewModel by viewModels()

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notes, container, false)

        toolbar = activity?.findViewById(R.id.my_toolbar)
        toolbarHead = activity?.findViewById(R.id.toolbar_head_edit)
        fabAdd = activity?.findViewById(R.id.fab_add_note)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerViewAdapter = NotesAdapter(requireContext(), this)

        toolbar?.title = null
        toolbarHead?.isFocusable = false

        toolbarHead?.setText(R.string.menu_search)
        val toolbarHeadLayout = toolbarHead?.layoutParams
        toolbarHeadLayout?.width = ViewGroup.LayoutParams.MATCH_PARENT
        toolbarHead?.layoutParams = toolbarHeadLayout
        Log.i("Back", toolbar?.background.toString())

        recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        recyclerView.adapter = recyclerViewAdapter

        //  Open the Search Fragment
        toolbarHead?.setOnClickListener {
            val fragment = SearchFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.content_fragment, fragment)
            transaction?.addToBackStack(null)
            transaction?.commit()
        }

        fabAdd?.visibility = View.VISIBLE
        fabAdd?.setOnClickListener {
            val intent = Intent(context, EditActivity::class.java)
            startActivity(intent)
        }

        notesViewModel.getAllNotes()
        notesViewModel.mAllNotes.observe(viewLifecycleOwner, {
            notes = it
            recyclerViewAdapter.setNotes(it)
        })

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
        fabAdd?.visibility = View.INVISIBLE
    }

    override fun onItemClick(position: Int, view: View?) {
        val intent = Intent(context, EditActivity::class.java)
        intent.putExtra("note", notes[position])
        startActivity(intent)
    }
}