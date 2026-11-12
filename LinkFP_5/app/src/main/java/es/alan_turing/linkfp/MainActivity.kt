package es.alan_turing.linkfp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import es.alan_turing.linkfp.ui.NavegacionApp
import es.alan_turing.linkfp.ui.theme.LinkFPTheme

/**
 * ==============================================================================================
 * CÓMO SE INTEGRA LA NAVEGACIÓN EN LA ACTIVIDAD PRINCIPAL (MainActivity)
 * ==============================================================================================
 *
 * 1. ¿QUÉ HACE ESTA CLASE?:
 *    `MainActivity` es el punto de entrada de la aplicación Android. Se encarga de inicializar la interfaz
 *    declarativa con Jetpack Compose mediante `setContent { ... }`.
 *
 * 2. ¿CÓMO FUNCIONA LA INTEGRACIÓN DE LA NAVEGACIÓN?:
 *    - `enableEdgeToEdge()` habilita el diseño que abarca toda la pantalla (incluidas las barras de estado y navegación).
 *    - `LinkFPTheme` aplica la paleta de colores y estilos de Material Design definidos en el proyecto.
 *    - `Scaffold` provee la estructura básica de pantalla de Material 3 y calcula el `innerPadding`
 *      (espacio necesario para no solapar la UI con las barras del sistema).
 *    - Dentro de `Scaffold`, llamamos a `NavegacionApp(modifier = Modifier.padding(innerPadding))`. De esta forma,
 *      toda la navegación de pantallas de la app se renderiza dentro del contenedor principal y respeta los insets de pantalla.
 * ==============================================================================================
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LinkFPTheme(dynamicColor = false) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Invocación del grafo de navegación pasando el padding seguro de Scaffold
                    NavegacionApp(
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}
