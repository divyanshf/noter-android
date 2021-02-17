package com.example.noter.ui.edit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.noter.R
import com.example.noter.data.model.Note

class EditActivity : AppCompatActivity() {
    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private var note:Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        titleEditText = findViewById(R.id.title_edit_text)
        contentEditText = findViewById(R.id.content_edit_text)

        try {
            note = intent.getParcelableExtra("note")!!
            titleEditText.setText(note?.title)
            contentEditText.setText(note?.content)
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        // save stuff
    }
}