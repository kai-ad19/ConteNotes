package com.example.contenotes
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import android.database.sqlite.SQLiteDatabase
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import org.w3c.dom.Text


class ListCaseActivity : ComponentActivity() {
    private lateinit var dbHelper: SQLDatabaseActivity // Initiate database for getting cases
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_caselist)

        val caseListContainer : LinearLayout = findViewById(R.id.caselistcontainer)

        dbHelper = SQLDatabaseActivity(this)
        val cursor = dbHelper.fetchAllCases()
        if (cursor != null){ // There are cases
            while (cursor.moveToNext()) { // Iterate through each record in the table
                // Get record values being used
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val accessed = cursor.getString(cursor.getColumnIndex("lastAccessed"))
                val colour = cursor.getString(cursor.getColumnIndex("colour"))
                val id = cursor.getInt(cursor.getColumnIndex("id"))

                // Generate new case frame
                val outerCaseFrame = LayoutInflater.from(this).inflate(R.layout.caseframe, null) as LinearLayout
                val innerCaseFrame = outerCaseFrame.findViewById<FrameLayout>(R.id.caseframe)
                val caseXML_Id = innerCaseFrame.findViewById<TextView>(R.id.caseid)
                val caseXML_Title = innerCaseFrame.findViewById<TextView>(R.id.casetitle)
                val caseXML_Accessed = innerCaseFrame.findViewById<TextView>(R.id.caseaccessed)
                val caseXML_Colour = innerCaseFrame.findViewById<FrameLayout>(R.id.colourtag)

                // Assign values
                val noteCount = dbHelper.getNotesCount(id)
                caseXML_Id.text = String.format("Case #%03d - %d Notes", id, noteCount)
                caseXML_Title.text = name
                caseXML_Accessed.text = accessed
                caseXML_Colour.setBackgroundColor(android.graphics.Color.parseColor("#$colour"))

                caseListContainer.addView(outerCaseFrame)

                // Connect the 'open' button to the 'View Notes' page.
                val openButton = innerCaseFrame.findViewById<Button>(R.id.btn_openCase)
                openButton.setOnClickListener{
                    val intent = Intent(this, ListNoteActivity::class.java)
                    intent.putExtra("CASE_ID", id)
                    startActivity(intent)
                }
            }
        }

        println("COMPLETED - LOADING PAGE")


        val headerText: TextView = findViewById(R.id.header_text)
        val returnButton: Button = findViewById(R.id.returnButton)
        val continueButton: Button = findViewById(R.id.continueButton)
        // 'middleButton' is unused on this page

        // Return button- go back to home page
        returnButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        headerText.text = "Your Cases"
        returnButton.text = "Menu"
        continueButton.text = "Create New"

        continueButton.setOnClickListener{ // Create a new case
            val intent = Intent(this, CreateCaseActivity::class.java)
            startActivity(intent)
        }

        // 'Open Case' is handled when generating the case list frames
    }
}