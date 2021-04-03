package com.example.noter.ui.main

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat.START
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.noter.R
import com.example.noter.data.viewmodel.NotesViewModel
import com.example.noter.ui.auth.AuthActivity
import com.example.noter.ui.settings.SettingsActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.Exception

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var toolbar: Toolbar
    private lateinit var toolbarHead: EditText
    private lateinit var drawer: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var fragment:Fragment
    private lateinit var sharedPreferences:SharedPreferences
    val notesViewModel:NotesViewModel by viewModels()
    private var theme:String? = ""
    private var user:FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //  Set theme as per preference
        val sp = this.getSharedPreferences("com.example.noter_preferences", 0)
        theme = sp.getString("theme_preference", "system")
        setTheme(theme)

        //  Check logged in user
        user = firebaseAuth.currentUser
        if(user == null){
            val authActivity = Intent(this, AuthActivity::class.java)
            authActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            authActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            authActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(authActivity)
        }
        else{
            //  Display content
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

            toolbar.setNavigationIcon(R.drawable.ic_baseline_dehaze_24)

            navigationView.setNavigationItemSelectedListener {
                onNavItemSelect(it)
            }

            initializeFragment()
        }
    }

    override fun onResume() {
        try {
            drawer.closeDrawer(START)
            navigationView.setNavigationItemSelectedListener {
                onNavItemSelect(it)
            }
            initializeFragment()
        }
        catch (e:Exception){
            e.printStackTrace()
        }

        super.onResume()
    }

    private fun initializeFragment(){
        navigationView.setCheckedItem(R.id.all_notes)
        fragment = NotesFragment()
        transactFragment()
    }

    private fun onNavItemSelect(menuItem: MenuItem) : Boolean{
        invalidateOptionsMenu()

        when(menuItem.itemId){
            R.id.all_notes -> {
                fragment = NotesFragment()
                transactFragment()
                drawer.closeDrawer(START)
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
            R.id.settings_item -> {
                startActivity(Intent(this, SettingsActivity::class.java))
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
        transaction.commit()
    }

    private fun transactFragmentSearch(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_fragment, fragment)
        transaction.commit()
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount == 0){
            super.onBackPressed()
        }
        else{
            supportFragmentManager.popBackStack()
        }
    }

    private fun setTheme(theme:String?){
        when(theme){
            "system" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            "dark" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            "light" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            else -> {
                Log.i("Theme Empty", "Why god why?")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
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
                transactFragmentSearch()
                true
            }
            R.id.account -> {
                MaterialAlertDialogBuilder(this, R.style.DialogAppearance)
                        .setTitle(user?.displayName)
                        .setMessage("Do you want to log out?")
                        .setPositiveButton("YES") { _: DialogInterface, _: Int ->
                            firebaseAuth.signOut()
                            val intent = Intent(this, AuthActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            startActivity(intent)
                        }
                        .setNegativeButton("NO", null)
                        .show()
                true
            }
            else -> false
        }
    }

}