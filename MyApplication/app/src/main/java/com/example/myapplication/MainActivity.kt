package com.example.myapplication

import android.content.res.Configuration
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
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            MyApplicationTheme {
//                Scaffold(
//                    modifier = Modifier.fillMaxSize()
//                ) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
//        }
//    }
//}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme() {
                Scaffold(
                    topBar = {
                        TopAppBar(title = { Text("Ejemplo") })
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
fun MyScreenContent() {
    Column {
        Text(text = "Hello World")
        Button(onClick = { /* acción */ }) {
            Text(text = "Click me")
        }
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
        Column () {
            Text (
                text = "${stringResource(R.string.contador)}: $count",
                fontSize = 25.sp,
                fontWeight = FontWeight.ExtraBold,
                fontStyle = FontStyle.Italic
            )
            Button(
                shape = MaterialTheme.shapes.small,
                onClick = { count++ }
            ) {
                Text(stringResource(R.string.aumentar))
            }
            Button(
                shape = MaterialTheme.shapes.small,
                onClick = { count-- }
            ) {
                Text(stringResource(R.string.disminuir))
            }
            MyButton (stringResource(R.string.mierda)) { count = 0 }
        }
    }
}

@Composable
fun MyButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
        color = Color.Red,
        fontStyle = FontStyle.Italic
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0x00AA2222,
    name = "Vista previa nocturna",
    locale = "es"
)
@Composable
fun MyCounterNight() {
    Counter()
}


@Preview(
    showBackground = true,
    backgroundColor = 0x00FFFFFF,
    name = "Vista previa principal"
)
@Composable
fun MyCounterPreview() {
    Counter()
}
