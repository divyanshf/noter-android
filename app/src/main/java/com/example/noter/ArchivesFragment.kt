package com.example.noter

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

class ArchivesFragment : Fragment() {
    private lateinit var toolbar: Toolbar
    private lateinit var toolbarHead: EditText
    private var drawer: DrawerLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_archives, container, false)

        toolbar = view.findViewById(R.id.my_toolbar)
        toolbarHead = view.findViewById(R.id.toolbar_head)

        //  Navigation drawer
        drawer = activity?.findViewById(R.id.drawer_layout)
        val drawerToggle = ActionBarDrawerToggle(activity, drawer, R.string.open, R.string.close)
        drawer?.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        //  Action bar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).actionBar?.title = null
        (activity as AppCompatActivity).actionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationIcon(R.drawable.ic_baseline_dehaze_24)

        toolbarHead.setText(R.string.archives)
        toolbarHead.setBackgroundResource(android.R.color.transparent)

        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                drawer?.openDrawer(GravityCompat.START)
                true
            }
            R.id.account -> {
                Toast.makeText(activity, "ACCOUNT", Toast.LENGTH_SHORT).show()
                true
            }
            else -> false
        }
    }

}