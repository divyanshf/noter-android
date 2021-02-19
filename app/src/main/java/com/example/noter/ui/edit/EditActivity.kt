package com.example.noter.ui.edit

import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import com.example.noter.R
import com.example.noter.data.model.Note
import com.example.noter.data.viewmodel.NotesViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditActivity : AppCompatActivity() {
    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var toolbar:Toolbar
    private lateinit var bottomAppBar: BottomAppBar
    private var intentNote:Note? = null
    private var prepopulate = false
    private var title = ""
    private var content = ""
    private val notesViewModel:NotesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        titleEditText = findViewById(R.id.title_edit_text)
        contentEditText = findViewById(R.id.content_edit_text)
        toolbar = findViewById(R.id.edit_toolbar)
        bottomAppBar = findViewById(R.id.edit_bottom_app_bar)

        try {
            intentNote = intent.getParcelableExtra("note")!!
            prepopulate = true
            title = intentNote?.title!!
            content = intentNote?.content!!
        }catch (e:Exception){
            e.printStackTrace()
        }

        titleEditText.setText(title)
        contentEditText.setText(content)

        setSupportActionBar(toolbar)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        titleEditText.doOnTextChanged { text, _, _, _ ->
            title = text.toString()
            supportActionBar?.title = title
        }

        contentEditText.doOnTextChanged { text, _, _, _ ->
            content = text.toString()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        saveNote()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                saveNote()
                finish()
                true
            }
            else -> false
        }
    }

    //  Save the note
    private fun saveNote(){
        when(prepopulate){
            true -> {
                if(title != intentNote?.title || content != intentNote?.content){
                    val newNote = Note(intentNote?.id, title, content, intentNote?.starred!!, intentNote?.archived!!, intentNote?.trash!!)
                    notesViewModel.updateNote(newNote)
                }
            }
            false -> {
                if(title.isNotEmpty() || content.isNotEmpty()){
                    val newNote = Note("", title, content, false, false, false)
                    notesViewModel.insert(newNote)
                }
            }
        }
    }
}