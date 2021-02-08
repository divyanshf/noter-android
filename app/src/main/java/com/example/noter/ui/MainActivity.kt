package com.example.noter.ui

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
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
import com.google.android.material.textfield.TextInputEditText


class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var toolbarHead: TextInputEditText
    private lateinit var drawer: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var fragment:Fragment
    private lateinit var sharedPreferences:SharedPreferences
    private var flagMenu:Boolean = true
    private val username:String? = null  //  Temporary variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("com.example.noter.ui", MODE_PRIVATE)

        toolbar = findViewById(R.id.my_toolbar)
        toolbarHead = findViewById(R.id.toolbar_head_edit)

        toolbarHead.setBackgroundResource(android.R.color.transparent)

        drawer = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)

        val drawerToggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        //  Action bar
        setSupportActionBar(toolbar)
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
        toolbar.setNavigationIcon(R.drawable.ic_baseline_dehaze_24)
        navigationView.setCheckedItem(R.id.all_notes)
        fragment = NotesFragment()
        transactFragment()
    }

    private fun onNavItemSelect(menuItem: MenuItem) : Boolean{
        hideKeyboard()
        toolbar.setNavigationIcon(R.drawable.ic_baseline_dehaze_24)
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
                if(sharedPreferences.getBoolean("Search", false)){
                    false
                }
                else{
                    drawer.openDrawer(START)
                    true
                }
            }
            R.id.menu_search -> {
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


    private fun hideKeyboard(){
        try {
            val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}