package com.example.contenotes
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity


class CreateNoteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_makecase)

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

        headerText.text = "Create Note - Case #---"
        returnButton.text = "Back"
        continueButton.text = "Finish"




        continueButton.setOnClickListener{
            val intent = Intent(this, ListNoteActivity::class.java)
            startActivity(intent)
        }
    }
}