package com.example.noter.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
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
class StarredFragment : Fragment() {
    private var toolbar: Toolbar? = null
    private var progressBar:ProgressBar? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyNotes: LinearLayout
    private lateinit var emptyNotesImageView: ImageView
    private lateinit var emptyNotesTextView: TextView
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
        toolbar?.setTitle(R.string.starred)
        emptyNotes = view.findViewById(R.id.empty_notes)
        emptyNotesImageView = view.findViewById(R.id.empty_notes_image_view)
        emptyNotesTextView = view.findViewById(R.id.empty_notes_text_view)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerViewAdapter = NotesAdapter(requireContext())
        progressBar = activity?.findViewById(R.id.progress_bar)

        emptyNotesImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_outline_star_outline_24, activity?.theme))
        emptyNotesTextView.text = "No Starred Notes"


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
        notesViewModel.getStarredNotes()
        notesViewModel.mStarredNotes.observe(viewLifecycleOwner, {
            when(it){
                Result.Progress -> {
                    progressBar?.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    progressBar?.visibility = View.GONE
                    notes = it.result
                    recyclerViewAdapter.setNotes(it.result)
                    if(notes.isEmpty()){
                        recyclerView.visibility = View.GONE
                        emptyNotes.visibility = View.VISIBLE
                    }
                    else{
                        recyclerView.visibility = View.VISIBLE
                        emptyNotes.visibility = View.GONE
                    }
                }
                is Result.Error -> {
                    progressBar?.visibility = View.GONE
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    recyclerView.visibility = View.GONE
                    emptyNotes.visibility = View.VISIBLE
                }
            }
        })
    }
}