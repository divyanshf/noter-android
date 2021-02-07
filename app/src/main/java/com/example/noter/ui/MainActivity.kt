package com.example.noter.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat.START
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.noter.R
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var toolbarHead: EditText
    private lateinit var drawer: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var fragment:Fragment
    private var flagMenu:Boolean = true
    private val username:String? = null  //  Temporary variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.my_toolbar)
        toolbarHead = findViewById(R.id.toolbar_head)
        drawer = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)

        val drawerToggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        //  Action bar
        setSupportActionBar(toolbar)
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationIcon(R.drawable.ic_baseline_dehaze_24)
        toolbarHead.setBackgroundResource(android.R.color.transparent)

        navigationView.setNavigationItemSelectedListener {
            onNavItemSelect(it)
        }
        initializeFragment()
    }

    override fun onRestart() {
        super.onRestart()
        navigationView.setNavigationItemSelectedListener {
            onNavItemSelect(it)
        }
        initializeFragment()
    }

    override fun onResume() {
        super.onResume()
        navigationView.setNavigationItemSelectedListener {
            onNavItemSelect(it)
        }
        initializeFragment()
    }

    private fun initializeFragment(){
        navigationView.setCheckedItem(R.id.all_notes)
        fragment = NotesFragment()
        transactFragment()
    }

    private fun onNavItemSelect(menuItem: MenuItem) : Boolean{
        flagMenu = false
        invalidateOptionsMenu()
        when(menuItem.itemId){
            R.id.all_notes -> {
                fragment = NotesFragment()
                transactFragment()
                drawer.closeDrawer(START)
                flagMenu = true
                invalidateOptionsMenu()
                return true
            }
            R.id.starred -> {
                fragment = StarredFragment()
                transactFragment()
                drawer.closeDrawer(START)
                return true
            }
            R.id.archives -> {
                fragment = ArchivesFragment()
                transactFragment()
                drawer.closeDrawer(START)
                return true
            }
            R.id.trash -> {
                fragment = TrashFragment()
                transactFragment()
                drawer.closeDrawer(START)
                return true
            }
            R.id.github_link -> {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/DivyanshFalodiya/noter-android"))
                startActivity(browserIntent)
                return true
            }
            else -> return false
        }
    }

    private fun transactFragment(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        if(flagMenu){
            menu?.findItem(R.id.menu_search)?.isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                drawer.openDrawer(START)
                true
            }
            R.id.menu_search -> {
                Toast.makeText(this, "Search icon", Toast.LENGTH_SHORT).show()
                fragment = SearchFragment()
                transactFragment()
                true
            }
            R.id.account -> {
                Toast.makeText(this, "ACCOUNT", Toast.LENGTH_SHORT).show()
                true
            }
            else -> false
        }
    }
}