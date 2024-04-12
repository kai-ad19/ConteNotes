package com.example.contenotes
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import android.database.sqlite.SQLiteDatabase


class ListCaseActivity : ComponentActivity() {
    private lateinit var dbHelper: SQLDatabaseActivity // Initiate database for getting cases
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        dbHelper = SQLDatabaseActivity(this)
        println("INITIATING DATABASE")
        val cursor = dbHelper.fetchAllCases()
        println(cursor)
        if (cursor != null){ // There are cases
            while (cursor.moveToNext()) { // Iterate through each record in the table
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val accessed = cursor.getString(cursor.getColumnIndex("lastAccessed"))
                val colour = cursor.getString(cursor.getColumnIndex("colour"))
                val id = cursor.getInt(cursor.getColumnIndex("id"))

                println("CASE")
                println(id)
                println(name)
                println(accessed)
                println(colour)
            }
        }

        println("COMPLETED - LOADING PAGE")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_caselist)


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





        continueButton.setOnClickListener{
            val intent = Intent(this, CreateCaseActivity::class.java)
            startActivity(intent)
        }
    }
}