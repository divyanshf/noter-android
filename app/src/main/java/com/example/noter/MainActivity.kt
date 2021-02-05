package com.example.noter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat.START
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var drawer: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var fragment:Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawer = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)

        val drawerToggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.all_notes -> {
                    Toast.makeText(this, "ALL NOTES", Toast.LENGTH_SHORT).show()
                    fragment = NotesFragment()
                    transactFragment()
                    true
                }
                R.id.starred -> {
                    Toast.makeText(this, "Starred", Toast.LENGTH_SHORT).show()
                    fragment = StarredFragment()
                    transactFragment()
                    true
                }
                R.id.archives -> {
                    Toast.makeText(this, "Archives", Toast.LENGTH_SHORT).show()
                    fragment = ArchivesFragment()
                    transactFragment()
                    true
                }
                R.id.trash -> {
                    Toast.makeText(this, "Trash", Toast.LENGTH_SHORT).show()
                    fragment = TrashFragment()
                    transactFragment()
                    true
                }
                R.id.github_link -> {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/DivyanshFalodiya/noter-android"))
                    startActivity(browserIntent)
                    true
                }
                else -> false
            }
        }
        initializeFragment()
    }

    private fun initializeFragment(){
        navigationView.setCheckedItem(R.id.all_notes)
        fragment = NotesFragment()
        transactFragment()
    }

    private fun transactFragment(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_toolbar, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when(item.itemId){
//            android.R.id.home -> {
//                drawer.openDrawer(START)
//                true
//            }
//            R.id.account -> {
//                Toast.makeText(this, "ACCOUNT", Toast.LENGTH_SHORT).show()
//                true
//            }
//            else -> false
//        }
//    }
}