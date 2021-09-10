package com.example.noter.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
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
import com.example.noter.ui.edit.EditActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

@AndroidEntryPoint
class NotesFragment : Fragment(){
    private var toolbar: Toolbar? = null
    private var toolbarHead: EditText? = null
    private var fabAdd: FloatingActionButton? = null
    private var progressBar:ProgressBar? = null
    private lateinit var emptyNotes:LinearLayout
    private lateinit var emptyNotesImageView:ImageView
    private lateinit var emptyNotesTextView:TextView
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

        val sp = activity?.getSharedPreferences("com.example.noter_preferences", 0)
        val listStyle = sp?.getString("recycler_view_preference", "2")

        toolbar = activity?.findViewById(R.id.my_toolbar)
        toolbarHead = activity?.findViewById(R.id.toolbar_head_edit)
        fabAdd = activity?.findViewById(R.id.fab_add_note)
        progressBar = activity?.findViewById(R.id.progress_bar)
        emptyNotes = view.findViewById(R.id.empty_notes)
        emptyNotesImageView = view.findViewById(R.id.empty_notes_image_view)
        emptyNotesTextView = view.findViewById(R.id.empty_notes_text_view)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerViewAdapter = NotesAdapter(requireContext())

        emptyNotesImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_outline_local_library_24, activity?.theme))
        emptyNotesTextView.text = "No Notes Created"

        toolbar?.title = null
        toolbarHead?.isFocusable = false

        toolbarHead?.setText(R.string.menu_search)
        val toolbarHeadLayout = toolbarHead?.layoutParams
        toolbarHeadLayout?.width = ViewGroup.LayoutParams.MATCH_PARENT
        toolbarHead?.layoutParams = toolbarHeadLayout
        Log.i("Back", toolbar?.background.toString())

        recyclerView.layoutManager = StaggeredGridLayoutManager(listStyle!!.toInt(), LinearLayoutManager.VERTICAL)
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

        refresh()

        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)
        swipeRefreshLayout.setOnRefreshListener {
            refresh()
            Timer("Refresh", false).schedule(500){
                swipeRefreshLayout.isRefreshing = false
            }
        }

        setHasOptionsMenu(true)

        return view
    }

    private fun refresh(){
        notesViewModel.getAllNotes()
        notesViewModel.mAllNotes.observe(viewLifecycleOwner, {
            when (it) {
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
                    Log.i("Notes", it.message)
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    recyclerView.visibility = View.GONE
                    emptyNotes.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.menu_search).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        val toolbarHeadLayout = toolbarHead?.layoutParams
        toolbarHeadLayout?.width = 0
        toolbarHead?.layoutParams = toolbarHeadLayout
        fabAdd?.visibility = View.INVISIBLE
        super.onDestroyView()
    }
}