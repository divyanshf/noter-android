package com.example.noter.ui.main

import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.noter.R
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var toolbar: Toolbar? = null
    private var toolbarHead: EditText? = null
    private var sharedPreferences:SharedPreferences? = null
    private var navigationView:NavigationView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        sharedPreferences = activity?.getSharedPreferences("com.example.noter.ui", AppCompatActivity.MODE_PRIVATE)
        sharedPreferences?.edit()?.putBoolean("Search", true)?.apply()

        toolbar = activity?.findViewById(R.id.my_toolbar)
        toolbarHead = activity?.findViewById(R.id.toolbar_head_edit)
        navigationView = activity?.findViewById(R.id.navigation_view)

        toolbarHead?.isFocusable = true
        toolbarHead?.isFocusableInTouchMode = true

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

        toolbar?.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)

        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.menu_search).isVisible = false
        menu.findItem(R.id.account).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedPreferences?.edit()?.putBoolean("Search", false)?.apply()

        toolbar?.setNavigationIcon(R.drawable.ic_baseline_dehaze_24)

        val toolbarHeadLayout = toolbarHead?.layoutParams
        toolbarHeadLayout?.width = 0
        toolbarHead?.layoutParams = toolbarHeadLayout

        val dp10 = getPixel(10F)
        val marginParamas = toolbar?.layoutParams as ViewGroup.MarginLayoutParams
        marginParamas.setMargins(dp10, dp10, dp10, dp10)
        toolbar?.setBackgroundResource(R.drawable.corner_background_toolbar)

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
                Log.i("BACK", "true")
                hideKeyboard()
                toolbar?.setNavigationIcon(R.drawable.ic_baseline_dehaze_24)
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