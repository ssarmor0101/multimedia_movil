package es.alan_turing.linkfp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import es.alan_turing.linkfp.ui.Configuracion
import es.alan_turing.linkfp.ui.Inicio
import es.alan_turing.linkfp.ui.theme.LinkFPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LinkFPTheme(dynamicColor = false) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    val NavegacionApp = NavegacionApp()
//
//                    NavegacionApp.NavHost(
//                        modifier = Modifier.padding(innerPadding)
//                    )
                }
            }
        }
    }
}