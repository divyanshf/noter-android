package com.example.noter.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.noter.R
import com.example.noter.data.model.Note
import com.example.noter.data.model.Result
import com.example.noter.data.viewmodel.NotesViewModel
import com.example.noter.ui.adapter.NotesAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

@AndroidEntryPoint
class TrashFragment : Fragment() {
    private var toolbar: Toolbar? = null
    private var progressBar:ProgressBar? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: NotesAdapter
    private val notesViewModel: NotesViewModel by viewModels()
    private var notes:List<Note> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notes, container, false)

        val sp = activity?.getSharedPreferences("com.example.noter_preferences", 0)
        val listStyle = sp?.getString("recycler_view_preference", "2")

        toolbar = activity?.findViewById(R.id.my_toolbar)
        toolbar?.setTitle(R.string.trash)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerViewAdapter = NotesAdapter(requireContext())
        progressBar = activity?.findViewById(R.id.progress_bar)


        recyclerView.layoutManager = StaggeredGridLayoutManager(listStyle!!.toInt(), LinearLayoutManager.VERTICAL)
        recyclerView.adapter = recyclerViewAdapter

        refresh()

        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)
        swipeRefreshLayout.setOnRefreshListener {
            refresh()
            Timer("Refresh", false).schedule(500){
                swipeRefreshLayout.isRefreshing = false
            }
        }

        return view
    }

    private fun refresh(){
        notesViewModel.getTrashNotes()
        notesViewModel.mTrashNotes.observe(viewLifecycleOwner, {
            when(it){
                Result.Progress -> {
                    progressBar?.visibility = View.VISIBLE
                    Log.i("Notes", "progress")
                }
                is Result.Success -> {
                    progressBar?.visibility = View.GONE
                    notes = it.result
                    recyclerViewAdapter.setNotes(it.result)
                }
                is Result.Error -> {
                    progressBar?.visibility = View.GONE
                    Log.i("Notes", it.message)
                }
            }
        })
    }

}