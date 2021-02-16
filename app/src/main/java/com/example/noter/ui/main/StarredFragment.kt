package com.example.noter.ui.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noter.R
import com.example.noter.data.viewmodel.NotesViewModel
import com.example.noter.ui.adapter.NotesAdapter
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StarredFragment : Fragment(), NotesAdapter.OnItemClickListener {
    private var toolbarHead: EditText? = null
    private var toolbar: Toolbar? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: NotesAdapter
    private val notesViewModel: NotesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_starred, container, false)

        toolbar = activity?.findViewById(R.id.my_toolbar)
        toolbar?.setTitle(R.string.starred)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerViewAdapter = NotesAdapter(requireContext(), this)

        recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        recyclerView.adapter = recyclerViewAdapter

        notesViewModel.getStarredNotes()
        notesViewModel.mStarredNotes.observe(viewLifecycleOwner, {
            Log.i("Notes", it.toString())
            recyclerViewAdapter.setNotes(it)
        })

        return view
    }

    override fun onItemClick(position: Int, view: View?) {
        Toast.makeText(context, "$position", Toast.LENGTH_SHORT).show()
    }
}