package com.example.contenotes
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.newSingleThreadContext
import org.w3c.dom.Text
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import java.security.MessageDigest
import kotlin.math.log

fun String.sha256(): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(this.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}

class CreateNoteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_makenote)
        val caseId = intent.getIntExtra("CASE_ID", -1)

        val headerText: TextView = findViewById(R.id.header_text)
        val returnButton: Button = findViewById(R.id.returnButton)
        val continueButton: Button = findViewById(R.id.continueButton)
        // 'middleButton' is unused on this page

        // Return button- go back to notes list page
        returnButton.setOnClickListener{
            val intent = Intent(this, ListNoteActivity::class.java)
            startActivity(intent)
            finish()
        }

        headerText.text = String.format("Create Note - Case #%03d",caseId)
        returnButton.text = "Cancel"
        continueButton.text = "Finish"

        // Saved values
        var note_title = ""
        var note_content = ""
        var note_hash = note_title.sha256()
        
        // Displayed fields
        val edit_note_title: EditText = findViewById(R.id.editText_noteTitle)
        val edit_note_content: EditText = findViewById(R.id.editText_noteContent)
        val shown_entry: TextView = findViewById(R.id.value_timestamp)
        val shown_hash: TextView = findViewById(R.id.value_hash)
        
        // Update hash & entry time instantly
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val currentTime = LocalDateTime.now().format(formatter)
        shown_entry.text = currentTime
        shown_hash.text = note_hash
        
        // Update hash when content is modified
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Update the hash when the text is changed
                note_content = edit_note_content.text.toString()
                note_hash = note_content.sha256()
                shown_hash.text = note_hash


                println("TextWatcher Text changed")
                println(note_hash)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        edit_note_content.addTextChangedListener(textWatcher)

        continueButton.setOnClickListener{
            // get values
            note_title = edit_note_title.text.toString().trim()
            note_content = edit_note_content.text.toString().trim()
            note_hash = note_content.sha256()

            // verify that title and description have been provided
            if (note_title.isNotEmpty() && note_content.isNotEmpty()){
                val dbHelper = SQLDatabaseActivity(this)
                val noteId = dbHelper.createNote(caseId, note_title, note_content, note_hash)
                val localNoteId = dbHelper.getNotesCount(caseId)
                Toast.makeText(this, String.format("Successfully added Case Note #%d\n(%d global note id)", localNoteId, noteId), Toast.LENGTH_SHORT).show()

                val intent = Intent(this, ListNoteActivity::class.java)
                intent.putExtra("CASE_ID", caseId)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Missing note Title or Content", Toast.LENGTH_SHORT).show()
            }
        }
    }
}