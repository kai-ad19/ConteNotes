package com.example.contenotes
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity


class ListNoteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_makecase)

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

        headerText.text = "Notes - Case #000 - 0 Notes"
        returnButton.text = "Back"
        continueButton.text = "Finish"


        middleButton.visibility = View.VISIBLE
        middleButton.setOnClickListener{
            val intent = Intent(this, ManageCaseActivity::class.java)
            startActivity(intent)
        }

        continueButton.setOnClickListener{
            val intent = Intent(this, ListCaseActivity::class.java)
            startActivity(intent)
        }
    }
}