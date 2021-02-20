package com.example.noter.ui.edit

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
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
    private var note:Note = Note("", "", "", starred = false, archived = false, trash = false)
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
            note = intentNote!!
        }catch (e:Exception){
            e.printStackTrace()
        }

        titleEditText.setText(note.title)
        contentEditText.setText(note.content)

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        titleEditText.doOnTextChanged { text, _, _, _ ->
            note.title = text.toString()
        }

        contentEditText.doOnTextChanged { text, _, _, _ ->
            note.content = text.toString()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        saveNote()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_toolbar, menu)
        setMenuOptions(menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                saveNote()
                finish()
                true
            }
            R.id.star_menu_item -> {
                note.starred = !note.starred
                if(note.starred){
                    Toast.makeText(this, "Note starred", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "Note unstarred", Toast.LENGTH_SHORT).show()
                }
                invalidateOptionsMenu()
                true
            }
            R.id.archive_menu_item -> {
                note.archived = !note.archived
                if(note.archived){
                    Toast.makeText(this, "Note archived", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "Note unarchived", Toast.LENGTH_SHORT).show()
                }
                saveNote()
                finish()
                true
            }
            R.id.trash_menu_item -> {
                note.trash = !note.trash
                if(note.trash){
                    Toast.makeText(this, "Note trashed", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "Note restored", Toast.LENGTH_SHORT).show()
                }
                saveNote()
                finish()
                true
            }
            R.id.trash_permanent_menu_item -> {
                notesViewModel.deleteNote(note)
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
                notesViewModel.updateNote(note)
            }
            false -> {
                if(note.title?.isNotBlank()!! || note.content?.isNotBlank()!!){
                    notesViewModel.insert(note)
                }
            }
        }
    }

    private fun setMenuOptions(menu:Menu?){
        //  Star
        if(note.starred){
            menu?.findItem(R.id.star_menu_item)?.setIcon(R.drawable.ic_baseline_star_24)
        }
        else{
            menu?.findItem(R.id.star_menu_item)?.setIcon(R.drawable.ic_outline_star_outline_24)
        }

        //  Archive
        if(note.archived){
            menu?.findItem(R.id.archive_menu_item)?.setIcon(R.drawable.ic_baseline_archive_24)
        }
        else{
            menu?.findItem(R.id.archive_menu_item)?.setIcon(R.drawable.ic_outline_archive_24)
        }

        //  Trash
        if(note.trash){
            menu?.findItem(R.id.trash_menu_item)?.title = "Restore note"
            menu?.findItem(R.id.trash_menu_item)?.setIcon(R.drawable.ic_baseline_delete_24)
            menu?.findItem(R.id.star_menu_item)?.isVisible = false
            menu?.findItem(R.id.archive_menu_item)?.isVisible = false
        }
        else{
            menu?.findItem(R.id.trash_menu_item)?.setIcon(R.drawable.ic_outline_delete_24)
            menu?.findItem(R.id.trash_menu_item)?.title = "Restore note"
            menu?.findItem(R.id.trash_permanent_menu_item)?.isVisible = false
        }
    }
}