package es.alan_turing.linkfp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import es.alan_turing.linkfp.model.RutasNavegacion

/**
 * ==============================================================================================
 * ¿QUÉ HACE ESTE COMPONENTE Y CÓMO FUNCIONA EL SISTEMA DE NAVEGACIÓN EN JETPACK COMPOSE?
 * ==============================================================================================
 *
 * 1. ¿QUÉ HACE?:
 *    `NavegacionApp` es el componente orquestador de la UI. Define el gráfico de navegación (NavGraph)
 *    de toda la aplicación, vinculando las rutas textuales ([RutasNavegacion]) con los componentes
 *    Componibles (@Composable) que representan cada pantalla.
 *
 * 2. ¿CÓMO FUNCIONA EL SISTEMA DE NAVEGACIÓN? (Explicación paso a paso):
 *
 *    A) EL CONTROLADOR DE NAVEGACIÓN (`NavController` / `NavHostController`):
 *       - Es el cerebro central de la navegación. Administra la pila de retroceso (Back Stack) y
 *         realiza las transiciones entre pantallas.
 *       - Se crea e inicializa mediante `rememberNavController()`, lo que garantiza que el objeto
 *         persista y conserve su estado durante la recomposición de la UI y los cambios de configuración.
 *
 *    B) EL CONTENEDOR DE NAVEGACIÓN (`NavHost`):
 *       - Es un contenedor Componible que actúa como un "marco de fotos" o "pantalla activa".
 *       - Recibe el `navController` y la ruta inicial (`startDestination`).
 *       - Muestra únicamente el Componible asociado a la ruta en la que se encuentra actualmente el
 *         `navController`.
 *
 *    C) EL GRÁFICO DE NAVEGACIÓN Y BLOQUES `composable(route)`:
 *       - Dentro del bloque lambda de `NavHost`, registramos cada pantalla mediante `composable(ruta) { ... }`.
 *       - Cuando invocamos `navController.navigate("configuracion")`, el `NavController` busca en este
 *         registro, añade la nueva pantalla a la pila de retroceso (Back Stack) y `NavHost` actualiza
 *         la pantalla visible.
 *       - Cuando invocamos `navController.popBackStack()`, la pantalla actual se elimina de la pila
 *         y se vuelve a mostrar la pantalla anterior en el historial.
 * ==============================================================================================
 *
 * @param modifier Modificador de diseño para ajustar márgenes e insets (Padding del Scaffold).
 * @param navController Instancia del controlador de navegación (por defecto `rememberNavController()`).
 * @param startDestination Ruta inicial donde arrancará la aplicación (por defecto `Principal`).
 */
@Composable
fun NavegacionApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = RutasNavegacion.Principal.route,
) {
    // NavHost vincula el controlador de navegación con el mapa/gráfico de pantallas
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        // Pantalla Principal (Menú)
        composable(RutasNavegacion.Principal.route) {
            Principal(navController = navController)
        }

        // Pantalla de Configuración y Preferencias
        composable(RutasNavegacion.Configuracion.route) {
            Configuracion(navController = navController)
        }

        // Pantalla Acerca De
        composable(RutasNavegacion.AcercaDe.route) {
            AcercaDe(navController = navController)
        }

        // Pantalla de Ayuda
        composable(RutasNavegacion.Ayuda.route) {
            Ayuda(navController = navController)
        }

        // Pantalla de Historia
        composable(RutasNavegacion.Historia.route) {
            Historia(navController = navController)
        }

        // Pantalla Sobre Nosotros
        composable(RutasNavegacion.SobreNosotros.route) {
            SobreNosotros(navController = navController)
        }
    }
}
