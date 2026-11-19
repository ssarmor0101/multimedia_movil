package es.alan_turing.linkfp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import es.alan_turing.linkfp.model.RutasNavegacion
import es.alan_turing.linkfp.ui.auth.InicioSesion
import es.alan_turing.linkfp.ui.auth.Registro
import es.alan_turing.linkfp.viewmodel.AuthViewModel

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
 *    B) COMPARTICIÓN DE VIEWMODEL DE AUTENTICACIÓN (AuthViewModel):
 *       - Al instanciar `authViewModel: AuthViewModel = viewModel()` en el ámbito de `NavegacionApp`,
 *         garantizamos que las pantallas [Principal], [InicioSesion] y [Registro] compartan la misma
 *         sesión de usuario activa sin recurrir a inyección de dependencias con Dagger Hilt.
 *
 *    C) EL CONTENEDOR DE NAVEGACIÓN (`NavHost`):
 *       - Recibe el `navController` y la ruta inicial (`startDestination`).
 *       - Registramos cada pantalla mediante `composable(ruta) { ... }`.
 * ==============================================================================================
 *
 * @param modifier Modificador de diseño para ajustar márgenes e insets (Padding del Scaffold).
 * @param navController Instancia del controlador de navegación (por defecto `rememberNavController()`).
 * @param authViewModel Instancia compartida del ViewModel de autenticación.
 * @param startDestination Ruta inicial donde arrancará la aplicación (por defecto `Principal`).
 */
@Composable
fun NavegacionApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = viewModel(),
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
            Principal(
                navController = navController,
                authViewModel = authViewModel,
            )
        }

        // Pantalla de Inicio de Sesión
        composable(RutasNavegacion.InicioSesion.route) {
            InicioSesion(
                navController = navController,
                authViewModel = authViewModel,
            )
        }

        // Pantalla de Registro de Usuario
        composable(RutasNavegacion.Registro.route) {
            Registro(
                navController = navController,
                authViewModel = authViewModel,
            )
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

        // Pantalla Pokédex
        composable(RutasNavegacion.Pokedex.route) {
            Pokedex(navController = navController)
        }
    }
}
