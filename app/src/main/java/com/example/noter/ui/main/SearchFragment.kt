package com.example.noter.ui.main

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
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
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var toolbar: Toolbar? = null
    private var appBar: AppBarLayout? = null
    private var toolbarHead: EditText? = null
    private var sharedPreferences:SharedPreferences? = null
    private var navigationView:NavigationView? = null
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var progressBar:ProgressBar? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: NotesAdapter
    private val notesViewModel: NotesViewModel by viewModels()
    private var notes:List<Note> = ArrayList()

    @SuppressLint("ResourceType")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notes, container, false)

        val sp = activity?.getSharedPreferences("com.example.noter_preferences", 0)
        val listStyle = sp?.getString("recycler_view_preference", "2")

        sharedPreferences = activity?.getSharedPreferences("com.example.noter.ui", AppCompatActivity.MODE_PRIVATE)
        sharedPreferences?.edit()?.putBoolean("Search", true)?.apply()

        toolbar = activity?.findViewById(R.id.my_toolbar)
        appBar = activity?.findViewById(R.id.my_app_bar)
        toolbarHead = activity?.findViewById(R.id.toolbar_head_edit)
        navigationView = activity?.findViewById(R.id.navigation_view)

        toolbarHead?.isFocusable = true
        toolbarHead?.isFocusableInTouchMode = true
        progressBar = activity?.findViewById(R.id.progress_bar)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerViewAdapter = NotesAdapter(requireContext())


        recyclerView.layoutManager = StaggeredGridLayoutManager(listStyle!!.toInt(), LinearLayoutManager.VERTICAL)
        recyclerView.adapter = recyclerViewAdapter

        toolbar?.title = null
        toolbarHead?.requestFocus()

        toolbarHead?.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if(hasFocus){
                openKeyboard()
            }
        }

        val toolbarHeadLayout = toolbarHead?.layoutParams
        toolbarHeadLayout?.width = ViewGroup.LayoutParams.MATCH_PARENT
        toolbarHead?.layoutParams = toolbarHeadLayout

        val marginParamas = toolbar?.layoutParams as ViewGroup.MarginLayoutParams
        marginParamas.setMargins(0,0,0,0)
        toolbar?.setBackgroundResource(R.drawable.background_toolbar)

        toolbarHead?.setText("")
        toolbarHead?.inputType = InputType.TYPE_TEXT_VARIATION_NORMAL
        toolbarHead?.setOnClickListener {
            openKeyboard()
        }

        toolbarHead?.doOnTextChanged { text, _, _, _ ->
            search(text.toString())
        }

        toolbar?.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh)
        swipeRefreshLayout.isEnabled = false
        swipeRefreshLayout.isRefreshing = false

        setHasOptionsMenu(true)

        return view
    }

    private fun search(text:String){
        if(text != ""){
            notesViewModel.searchNotes(text)

            try {
                notesViewModel.mSearchNotes.observe(viewLifecycleOwner, {
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
            catch (e:Exception){
                e.printStackTrace()
            }
        }
        else{
            recyclerViewAdapter.setNotes(ArrayList())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.menu_search).isVisible = false
        menu.findItem(R.id.account).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        sharedPreferences?.edit()?.putBoolean("Search", false)?.apply()


        toolbar?.setNavigationIcon(R.drawable.ic_baseline_dehaze_24)

        val toolbarHeadLayout = toolbarHead?.layoutParams
        toolbarHeadLayout?.width = 0
        toolbarHead?.layoutParams = toolbarHeadLayout

        notesViewModel.mSearchNotes.removeObservers(viewLifecycleOwner)

        swipeRefreshLayout.isEnabled = true
        swipeRefreshLayout.isRefreshing = true

        val dp10 = getPixel(10F)
        val dp15 = getPixel(15F)
        val marginParamas = toolbar?.layoutParams as ViewGroup.MarginLayoutParams
        marginParamas.setMargins(dp15, dp10, dp15, dp10)
        toolbar?.setBackgroundResource(R.drawable.corner_background_toolbar)
        super.onDestroyView()
    }

    private fun getPixel(dp:Float):Int{
        val resources = context?.resources
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources?.displayMetrics
        ).toInt()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                Log.i("Search", "true")
                hideKeyboard()
                navigationView?.setCheckedItem(R.id.all_notes)
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                val fragment = NotesFragment()
                transaction?.replace(R.id.content_fragment, fragment)?.commit()
                true
            }
            else -> false
        }
    }

    private fun hideKeyboard(){
        try {
            val imm: InputMethodManager = activity?.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun openKeyboard(){
        try {
            Log.i("Keyboard", "Open")
            val imm: InputMethodManager = activity?.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


}