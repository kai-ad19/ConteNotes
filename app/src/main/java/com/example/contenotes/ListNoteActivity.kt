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
import androidx.activity.ComponentActivity
import java.io.Serializable


class ListNoteActivity : ComponentActivity() {
    private lateinit var dbHelper: SQLDatabaseActivity // Initiate database for getting notes
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_notelist)
        dbHelper = SQLDatabaseActivity(this)
        val caseId = intent.getIntExtra("CASE_ID", -1)
        val noteCount = dbHelper.getNotesCount(caseId)

        val noteListContainer : LinearLayout = findViewById(R.id.notelistcontainer)
        val cursor = dbHelper.fetchNotes(caseId)
        if (cursor != null) { // There are notes associated with this case
            while (cursor.moveToNext()) { // Iterate through each record in the table
                // Get values from the table
                val title = cursor.getString(cursor.getColumnIndex("title"))
                val created = cursor.getString(cursor.getColumnIndex("created"))
                val hashvalue = cursor.getString(cursor.getColumnIndex("hashvalue"))
                val localid = cursor.getInt(cursor.getColumnIndex("local_note_id"))
                val globalid = cursor.getInt(cursor.getColumnIndex("note_id"))

                // Generate new note frame
                val outerNoteFrame = LayoutInflater.from(this).inflate(R.layout.noteframe, null) as LinearLayout
                val innerNoteFrame = outerNoteFrame.findViewById<FrameLayout>(R.id.noteframe)
                val noteXML_title = innerNoteFrame.findViewById<TextView>(R.id.noteTitle)
                val noteXML_created = innerNoteFrame.findViewById<TextView>(R.id.noteCreated)
                val noteXML_hashValue = innerNoteFrame.findViewById<TextView>(R.id.hashvalue)

                // Assign values
                noteXML_title.text = String.format("Note #%03d - %s", localid, title)
                noteXML_created.text = String.format("Created: %s", created)
                noteXML_hashValue.text = hashvalue

                noteListContainer.addView(outerNoteFrame)

                // Connect the 'open' button to the 'View Notes' page.
                val openButton = innerNoteFrame.findViewById<Button>(R.id.btn_readNote)
                openButton.setOnClickListener{
                    println("lol")
                    val intent = Intent(this, ReadNoteActivity::class.java)
                    intent.putExtra("CASE_ID", caseId)
                    intent.putExtra("NOTE_GLOBAL_ID", globalid)
                    startActivity(intent)
                }
            }
        }

        val headerText: TextView = findViewById(R.id.header_text)
        val returnButton: Button = findViewById(R.id.returnButton)
        val continueButton: Button = findViewById(R.id.continueButton)
        val middleButton: Button = findViewById(R.id.middleButton) // Used for 'manage'

        // Return button- go back to case list page
        returnButton.setOnClickListener{
            val intent = Intent(this, ListCaseActivity::class.java)
            startActivity(intent)
            finish()
        }

        headerText.text = String.format("Case #%03d - %d Notes", caseId, noteCount)
        returnButton.text = "Back"
        continueButton.text = "Add Note"
        middleButton.text = "Manage"


        middleButton.visibility = View.VISIBLE
        middleButton.setOnClickListener{
            val intent = Intent(this, ManageCaseActivity::class.java)
            intent.putExtra("CASE_ID", caseId)
            startActivity(intent)
        }

        continueButton.setOnClickListener{
            val intent = Intent(this, CreateNoteActivity::class.java)
            intent.putExtra("CASE_ID", caseId)
            startActivity(intent)
        }
    }
}