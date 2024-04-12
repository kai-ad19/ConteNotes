package com.example.contenotes
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import java.io.Serializable

class ReadNoteActivity : ComponentActivity() {
    private lateinit var dbHelper: SQLDatabaseActivity // Initiate database for getting notes

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_readnote)
        dbHelper = SQLDatabaseActivity(this)
        val caseId = intent.getIntExtra("CASE_ID", -1)
        val global_noteId = intent.getIntExtra("NOTE_GLOBAL_ID", -1)

        // Get note data and set up the page
        val cursor = dbHelper.fetchSingleNote(global_noteId)
        if (cursor != null){
            if (cursor.moveToFirst()){
                val local_noteId = cursor.getInt(cursor.getColumnIndex("local_note_id"))
                val title = cursor.getString(cursor.getColumnIndex("title"))
                val content = cursor.getString(cursor.getColumnIndex("content"))
                val created = cursor.getString(cursor.getColumnIndex("created"))
                val hashvalue = cursor.getString(cursor.getColumnIndex("hashvalue"))

                val XMLnoteTitle: TextView = findViewById(R.id.editText_noteTitle)
                val XMLnoteContent: TextView = findViewById(R.id.editText_noteContent)
                val XMLnoteCreated: TextView = findViewById(R.id.value_timestamp)
                val XMLnoteHash: TextView = findViewById(R.id.value_hash)

                XMLnoteTitle.text = title
                XMLnoteContent.text = content
                XMLnoteCreated.text = created
                XMLnoteHash.text = hashvalue

                val headerText: TextView = findViewById(R.id.header_text)
                val returnButton: Button = findViewById(R.id.returnButton)
                val continueButton: Button = findViewById(R.id.continueButton) // Used for 'delete'
                //val middleButton: Button = findViewById(R.id.middleButton) - ContinueButton is used instead

                // Return button- go back to case list page
                returnButton.setOnClickListener{
                    val intent = Intent(this, ListNoteActivity::class.java)
                    intent.putExtra("CASE_ID", caseId)
                    startActivity(intent)
                    finish()
                }

                // Set header & button names
                headerText.text = String.format("Case #%03d - Note #%03d (#%03d Global)", caseId, local_noteId, global_noteId)
                returnButton.text = "Back"
                continueButton.text = "Delete"

                continueButton.setOnClickListener{
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Confirm Deletion")
                    builder.setMessage(String.format("Are you sure you want to delete Note #%03d? This action cannot be undone.", local_noteId))

                    builder.setPositiveButton("Delete") {dialog, which ->
                        dbHelper.deleteNote(global_noteId)
                        val intent = Intent(this, ListNoteActivity::class.java)
                        intent.putExtra("CASE_ID", caseId)
                        startActivity(intent)
                        Toast.makeText(this, String.format("Successfully deleted Note #%03d from Case #%03d", local_noteId, caseId), Toast.LENGTH_SHORT).show()
                    }

                    builder.setNegativeButton("Cancel") {dialog, which ->
                        // Don't do anything
                    }

                    val dialog = builder.create()
                    dialog.show()
                }
            } else {
                Toast.makeText(this, "An error occured trying to open this note (NO ROWS).", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "An error occured trying to open this note (CURSOR NULL).", Toast.LENGTH_SHORT).show()
        }


    }
}