package com.example.noter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat.START
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var menuSearch: TextView
    private lateinit var drawer: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.my_toolbar)
        menuSearch = findViewById(R.id.menu_search)
        drawer = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)

        val drawerToggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {
            onOptionsItemSelected(it)
        }

        setSupportActionBar(toolbar)
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationIcon(R.drawable.ic_baseline_dehaze_24)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                drawer.openDrawer(START)
                true
            }
            R.id.all_notes -> {
                Toast.makeText(this, "ALL NOTES", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.starred -> {
                Toast.makeText(this, "Starred", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.archives -> {
                Toast.makeText(this, "Archives", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.trash -> {
                Toast.makeText(this, "Trash", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.github_link -> {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/DivyanshFalodiya/noter-web"))
                startActivity(browserIntent)
                true
            }
            R.id.account -> {
                Toast.makeText(this, "ACCOUNT", Toast.LENGTH_SHORT).show()
                true
            }
            else -> false
        }
    }

    fun openSearchActivity(view: View){
        Toast.makeText(this, "SEARCH", Toast.LENGTH_SHORT).show()
    }
}