package com.example.contenotes
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity


class ManageCaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_makecase)

        val headerText: TextView = findViewById(R.id.header_text)
        val returnButton: Button = findViewById(R.id.returnButton)
        val continueButton: Button = findViewById(R.id.continueButton)
        // 'middleButton' is unused on this page

        // Return button- go back to note list page
        returnButton.setOnClickListener{
            val intent = Intent(this, ListNoteActivity::class.java)
            startActivity(intent)
            finish()
        }

        headerText.text = "Manage Case #---"
        returnButton.text = "Back"
        continueButton.text = "Save"




        continueButton.setOnClickListener{
            val intent = Intent(this, ListNoteActivity::class.java)
            startActivity(intent)
        }
    }
}