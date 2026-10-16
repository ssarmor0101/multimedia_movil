package es.fpalanturing.corrutinas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import es.fpalanturing.corrutinas.ui.theme.CorrutinasTheme
import es.fpalanturing.corrutinas.ui.Corrutinas

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CorrutinasTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Corrutinas(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
