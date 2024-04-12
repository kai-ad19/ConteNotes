package com.example.contenotes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.contenotes.ui.theme.ConteNotesTheme
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MainActivity : ComponentActivity() {
    private lateinit var dbHelper: SQLDatabaseActivity
    private lateinit var db: SQLiteDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        // Initiate and set the content view
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Go to the homepage on launch

        // Run the initial database operations
        val dbHelper = SQLDatabaseActivity(this)
        val db = dbHelper.writableDatabase

        val viewcasesButton: Button = findViewById(R.id.btn_page_caselist)
        viewcasesButton.setOnClickListener{
            val intent = Intent(this, ListCaseActivity::class.java)
            startActivity(intent)
        }


        // Below is the default when creating this app- only commented out in case it's needed later.
        //setContent {
        //    ConteNotesTheme {
        //        // A surface container using the 'background' color from the theme
        //        Surface(
        //            modifier = Modifier.fillMaxSize(),
        //            color = MaterialTheme.colorScheme.background
        //        ) {
        //            Greeting("Android")
        //        }
        //    }
        //}
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ConteNotesTheme {
        Greeting("Android")
    }
}