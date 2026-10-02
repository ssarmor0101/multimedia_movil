package com.example.myapplication2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication2.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme() {
                Scaffold(
                    topBar = {
                        TopAppBar(title = { Text("Scaffold Example") })
                    },
                    floatingActionButton = {
                        FloatingActionButton(onClick = { /* Acción */ }) {
                            Icon(painterResource(R.drawable.ic_launcher_foreground), contentDescription = "Add")
                        }
                    }
                ) {
                        innerPadding ->
                    Counter(modifier = Modifier.padding(innerPadding));
                }
            }
        }
    }
}

@Composable
fun MyApp() {
    MaterialTheme {
        Counter()
    }
}

@Composable
fun Counter(modifier: Modifier = Modifier) {
    var count by remember { mutableStateOf(0) }
    Column (
        modifier = modifier.fillMaxHeight().fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column (
            modifier = Modifier.border(1.dp, Color.Black)
        ) {
            Text (
                text = "Count: $count",
                fontSize = 25.sp,
                color = Color.Magenta,
                fontWeight = FontWeight.ExtraBold,
                fontStyle = FontStyle.Italic
            )
            Button(onClick = { count++ }) {
                Text("Increase Count")
            }
            Button(onClick = { count-- }) {
                Text("Decrease Count")
            }
            MyButton { count = 0 }
        }
    }
}

@Composable
fun MyButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("Shit")
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
    MyApplicationTheme {
        Greeting("Android")
    }
}