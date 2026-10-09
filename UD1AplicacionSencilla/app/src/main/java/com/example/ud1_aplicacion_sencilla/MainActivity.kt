package com.example.ud1_aplicacion_sencilla

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.ud1_aplicacion_sencilla.ui.AplicacionSencilla
import com.example.ud1_aplicacion_sencilla.ui.Contenido
import com.example.ud1_aplicacion_sencilla.ui.theme.UD1AplicacionSencillaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UD1AplicacionSencillaTheme(
                dynamicColor = false
            ) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AplicacionSencilla(
                        modifier = Modifier.padding(innerPadding)
                    );
                }
            }
        }
    }
}

//@Preview(
//    name = "Modo claro",
//    showBackground = true,
//    locale = "es"
//)
//@Preview(
//    name = "Modo oscuro",
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES
//)
//@Composable
//fun AplicacionPreview() {
//    UD1AplicacionSencillaTheme(
//        dynamicColor = false
//    ) {
//        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//            AplicacionSencilla(
//                modifier = Modifier.padding(innerPadding)
//            );
//        }
//    }
//}

@PreviewLightDark
@Composable
fun AplicacionPreview() {
    UD1AplicacionSencillaTheme(
        dynamicColor = false
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            AplicacionSencilla(
                modifier = Modifier.padding(innerPadding)
            );
        }
    }
}

@PreviewLightDark
@Composable
fun AplicacionPreviewActive() {
    UD1AplicacionSencillaTheme(
        dynamicColor = false
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Contenido(
                pulsado = true,
                onPress = {},
                onReset = {},
                modifier = Modifier.padding(innerPadding)
            );
        }
    }
}