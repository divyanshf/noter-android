package com.example.noter.ui.edit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.noter.R
import com.example.noter.data.model.Note
import com.google.android.material.bottomappbar.BottomAppBar

class EditActivity : AppCompatActivity() {
    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var toolbar:Toolbar
    private lateinit var bottomAppBar: BottomAppBar
    private var note:Note? = null
    private var title = ""
    private var content = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        titleEditText = findViewById(R.id.title_edit_text)
        contentEditText = findViewById(R.id.content_edit_text)
        toolbar = findViewById(R.id.edit_toolbar)
        bottomAppBar = findViewById(R.id.edit_bottom_app_bar)

        try {
            note = intent.getParcelableExtra("note")!!
            title = note?.title!!
            content = note?.content!!
        }catch (e:Exception){
            e.printStackTrace()
        }

        titleEditText.setText(note?.title)
        contentEditText.setText(note?.content)

        setSupportActionBar(toolbar)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                Toast.makeText(this, "cancel and finish", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.submit_menu_item -> {
                Toast.makeText(this, "save and finish", Toast.LENGTH_SHORT).show()
                true
            }
            else -> false
        }
    }

}