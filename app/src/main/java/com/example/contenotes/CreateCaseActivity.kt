package com.example.contenotes
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import kotlinx.coroutines.handleCoroutineException
import android.graphics.Color
import android.widget.Toast

class CreateCaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_makecase)

        val headerText: TextView = findViewById(R.id.header_text)
        val returnButton: Button = findViewById(R.id.returnButton)
        val continueButton: Button = findViewById(R.id.continueButton)
        // 'middleButton' is unused on this page

        // Return button- go back to case list page
        returnButton.setOnClickListener{
            val intent = Intent(this, ListCaseActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 'Page setup' - Assign header text, return and continue button titles.
        headerText.text = "Creating New Case"
        returnButton.text = "Back"
        continueButton.text = "Finish"


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


        continueButton.setOnClickListener{
            // get values
            val input_title = edit_caseTitle.text.toString().trim()
            val input_description = edit_caseDescription.text.toString().trim()
            input_hexcode = edit_caseColour.text.toString().trim()
            input_hexcode = input_hexcode.padEnd(6, '0')

            // verify that title and description have been provided
            if (input_title.isNotEmpty() && input_description.isNotEmpty()){
                val dbHelper = SQLDatabaseActivity(this)
                dbHelper.newCase(input_title, input_description, input_hexcode)

                val intent = Intent(this, ListCaseActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Missing case Title or Description", Toast.LENGTH_SHORT).show()
            }


        }
    }
}
