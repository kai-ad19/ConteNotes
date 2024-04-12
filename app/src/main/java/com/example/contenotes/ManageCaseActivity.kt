package com.example.contenotes
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity


class ManageCaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_managecase)
        val caseId = intent.getIntExtra("CASE_ID", -1)
        val dbHelper = SQLDatabaseActivity(this)

        val headerText: TextView = findViewById(R.id.header_text)
        val returnButton: Button = findViewById(R.id.returnButton)
        val continueButton: Button = findViewById(R.id.continueButton)
        // 'middleButton' is unused on this page

        // Return button- go back to note list page
        returnButton.setOnClickListener{
            val intent = Intent(this, ListNoteActivity::class.java)
            intent.putExtra("CASE_ID", caseId)
            startActivity(intent)
            finish()
        }

        headerText.text = String.format("Manage Case #%03d", caseId)
        returnButton.text = "Back"
        continueButton.text = "Save"

        // Text Fields
        val edit_caseTitle: EditText = findViewById(R.id.editText_caseTitle)
        val edit_caseDescription: EditText = findViewById(R.id.editText_caseDescription)
        val edit_caseColour: EditText = findViewById(R.id.editText_caseColour)
        val frame_colourTag: FrameLayout = findViewById(R.id.colourtag)
        var input_hexcode = "000000"

        // Update 'colour tag' when the hex code editor loses focus
        edit_caseColour.setOnFocusChangeListener{ _, hasFocus ->
            if(!hasFocus){
                input_hexcode = edit_caseColour.text.toString().trim()
                if (input_hexcode.length < 6){
                    input_hexcode = input_hexcode.padEnd(6, '0') // Replace missing values with 0
                }
                frame_colourTag.setBackgroundColor(android.graphics.Color.parseColor("#$input_hexcode"))
            }
        }

        // 'Save' button - update the SQL. Instead of going to case list, return to notes
        continueButton.setOnClickListener{
            // get values
            val input_title = edit_caseTitle.text.toString().trim()
            val input_description = edit_caseDescription.text.toString().trim()
            input_hexcode = edit_caseColour.text.toString().trim()
            input_hexcode = input_hexcode.padEnd(6, '0')

            // verify that title and description have been provided
            if (input_title.isNotEmpty() && input_description.isNotEmpty()){
                dbHelper.updateCase(caseId, input_title, input_description, input_hexcode)

                val intent = Intent(this, ListNoteActivity::class.java)
                intent.putExtra("CASE_ID", caseId)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Missing case Title or Description", Toast.LENGTH_SHORT).show()
            }
        }

        // 'Export Notes' button


        // 'Delete Case' button
        val deleteCaseButton: Button = findViewById(R.id.btn_DeleteCase)
        deleteCaseButton.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirm Deletion")
            builder.setMessage(String.format("Are you sure you want to delete Case #%03d? This action cannot be undone.", caseId))

            builder.setPositiveButton("Delete") {dialog, which ->
                dbHelper.deleteCase(caseId)
                val intent = Intent(this, ListCaseActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, String.format("Successfully deleted Case #%03d", caseId), Toast.LENGTH_SHORT).show()
            }

            builder.setNegativeButton("Cancel") {dialog, which ->
                // Don't do anything
            }

            val dialog = builder.create()
            dialog.show()
        }
    }
}