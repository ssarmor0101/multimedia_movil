package es.alan_turing.linkfp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import es.alan_turing.linkfp.ui.Inicio
import es.alan_turing.linkfp.ui.Pantalla2
import es.alan_turing.linkfp.ui.theme.LinkFPTheme

class SegundaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LinkFPTheme(dynamicColor = false) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Pantalla2(modifier = Modifier.padding(innerPadding));
                }
            }
        }
    }
}