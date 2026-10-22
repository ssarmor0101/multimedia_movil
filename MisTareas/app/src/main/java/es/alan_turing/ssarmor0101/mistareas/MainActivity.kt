package es.alan_turing.ssarmor0101.mistareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import es.alan_turing.ssarmor0101.mistareas.ui.TarjetasTareas
import es.alan_turing.ssarmor0101.mistareas.ui.theme.MisTareasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MisTareasTheme(
                dynamicColor = false
            ) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TarjetasTareas(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}