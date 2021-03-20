package com.example.noter.ui.edit

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import com.example.noter.R
import com.example.noter.data.model.Note
import com.example.noter.data.viewmodel.NotesViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Timestamp
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

@AndroidEntryPoint
class EditActivity : AppCompatActivity() {
    private lateinit var titleEditText: EditText
    private lateinit var editTime: TextView
    private lateinit var contentEditText: EditText
    private lateinit var toolbar:Toolbar
    private lateinit var bottomAppBar: BottomAppBar
    private var intentNote:Note? = null
    private var prepopulate = false
    private var note:Note = Note("", "", "", starred = false, archived = false, trash = false, edited = false, timestamp = null)
    private val notesViewModel:NotesViewModel by viewModels()

    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        titleEditText = findViewById(R.id.title_edit_text)
        editTime = findViewById(R.id.edit_time)
        contentEditText = findViewById(R.id.content_edit_text)
        toolbar = findViewById(R.id.edit_toolbar)
        bottomAppBar = findViewById(R.id.edit_bottom_app_bar)

        try {
            intentNote = intent.getParcelableExtra("note")!!
            prepopulate = true
            note = Note(intentNote?.id, intentNote?.title, intentNote?.content, intentNote?.starred!!, intentNote?.archived!!, intentNote?.trash!!, intentNote?.edited!!, intentNote?.timestamp)
            val date = note.timestamp?.toDate()
            val day = getDay(date!!)
            if(note.edited){
                editTime.text = "Edited $day"
            }
            else{
                editTime.text = "Created $day"
            }
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

    @ExperimentalTime
    private fun getDay(date:Date):String{
        val timeMS = date.time.milliseconds.toLongMilliseconds()
        val timeCalendar = Calendar.getInstance()
        timeCalendar.timeInMillis = timeMS

        val now = Calendar.getInstance().get(Calendar.DATE)
        return when(timeCalendar.get(Calendar.DATE)){
            now -> {
                date.toString().substring(11, 16)
            }
            else -> {
                val timestampString = date.toString()
                val date = timestampString.substring(4, 10)
                val year = timestampString.substring(timestampString.length - 4, timestampString.length)
                return "$date, $year"
            }
        }
    }

    override fun onBackPressed() {
        saveNote()
        super.onBackPressed()
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
                MaterialAlertDialogBuilder(this, R.style.DialogAppearance)
                        .setTitle(R.string.delete_permanent)
                        .setMessage("Are you sure ?")
                        .setPositiveButton("YES") { _: DialogInterface, _: Int ->
                            notesViewModel.deleteNote(note)
                            finish()
                        }
                        .setNegativeButton("NO", null)
                        .show()
                true
            }
            else -> false
        }
    }

    //  Save the note
    private fun saveNote(){
        when(prepopulate){
            true -> {
                if(note != intentNote){
                    note.edited = true
                    note.timestamp = Timestamp.now()
                    notesViewModel.updateNote(note)
                }
            }
            false -> {
                if(note.title?.isNotBlank()!! || note.content?.isNotBlank()!!){
                    note.timestamp = Timestamp.now()
                    notesViewModel.insert(note)
                }
            }
        }
    }

    private fun setMenuOptions(menu:Menu?){
        //  Star
        if(note.starred){
            menu?.findItem(R.id.star_menu_item)?.title = "Unstar"
            menu?.findItem(R.id.star_menu_item)?.setIcon(R.drawable.ic_baseline_star_24)
        }
        else{
            menu?.findItem(R.id.star_menu_item)?.title = "Star"
            menu?.findItem(R.id.star_menu_item)?.setIcon(R.drawable.ic_outline_star_outline_24)
        }

        //  Archive
        if(note.archived){
            menu?.findItem(R.id.archive_menu_item)?.title = "Unarchive"
            menu?.findItem(R.id.archive_menu_item)?.setIcon(R.drawable.ic_baseline_unarchive_24)
        }
        else{
            menu?.findItem(R.id.archive_menu_item)?.title = "Archive"
            menu?.findItem(R.id.archive_menu_item)?.setIcon(R.drawable.ic_outline_archive_24)
        }

        //  Trash
        if(note.trash){
            menu?.findItem(R.id.trash_menu_item)?.title = "Restore note"
            menu?.findItem(R.id.trash_menu_item)?.setIcon(R.drawable.ic_baseline_restore_from_trash_24)
            menu?.findItem(R.id.star_menu_item)?.isVisible = false
            menu?.findItem(R.id.archive_menu_item)?.isVisible = false
        }
        else{
            menu?.findItem(R.id.trash_menu_item)?.title = "Delete note"
            menu?.findItem(R.id.trash_menu_item)?.setIcon(R.drawable.ic_outline_delete_24)
            menu?.findItem(R.id.trash_permanent_menu_item)?.isVisible = false
        }
    }
}