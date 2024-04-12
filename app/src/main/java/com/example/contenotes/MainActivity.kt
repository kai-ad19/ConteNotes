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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Go to the homepage on launch

        val viewcasesButton: Button = findViewById(R.id.btn_page_caselist)
        viewcasesButton.setOnClickListener{
            val intent = Intent(this, CaseListActivity::class.java)
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