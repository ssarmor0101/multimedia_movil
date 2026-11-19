package es.alan_turing.linkfp.ui

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CatchingPokemon
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import es.alan_turing.linkfp.R
import es.alan_turing.linkfp.model.RutasNavegacion
import es.alan_turing.linkfp.viewmodel.AuthViewModel

/**
 * ==============================================================================================
 * CÓMO FUNCIONA LA NAVEGACIÓN Y EL ESTADO DE SESIÓN EN LA PANTALLA PRINCIPAL
 * ==============================================================================================
 *
 * 1. ¿QUÉ HACE ESTA PANTALLA?:
 *    Muestra el menú principal de la aplicación mediante tarjetas navegables [ElevatedCard] y un
 *    panel superior de control de sesión de usuario autenticado con Firebase.
 *
 * 2. FUNCIONAMIENTO PEDAGÓGICO:
 *    - **Control de Sesión**: Lee la propiedad `currentUser` emitida por [AuthViewModel.currentUser] mediante
 *      `collectAsState()`.
 *    - **Visualización Condicional de Usuario**: Muestra un componente [Text] indicando el correo del
 *      usuario activo si hay una sesión iniciada, o bien "Sesión no iniciada" si no hay ninguna.
 *    - **Acción con TextButton**: Muestra un [TextButton] para permitir al usuario iniciar sesión
 *      (navegando a `RutasNavegacion.InicioSesion.route`) o bien finalizar la sesión actual ([AuthViewModel.logout]).
 *    - **Navegación general**: Al hacer clic en cualquier tarjeta de opción, utiliza `navController.navigate(...)`.
 * ==============================================================================================
 *
 * @param navController Controlador de navegación para ejecutar `navigate()`.
 * @param modifier Modificador visual para layout e insets.
 * @param authViewModel Instancia del ViewModel de autenticación (por defecto `viewModel()`).
 */
@Composable
fun Principal(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel(),
) {
    val context = LocalContext.current
    var showExitDialog by remember { mutableStateOf(value = false) }

    // Observamos la sesión del usuario actual desde Firebase
    val currentUser by authViewModel.currentUser.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Encabezado con el título de la pantalla
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(R.string.menu_principal),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        // ==========================================================================================
        // TARJETA DE ESTADO DE SESIÓN Y BOTÓN DE INICIO / FINALIZACIÓN DE SESIÓN (REQUISITO RÚBRICA)
        // ==========================================================================================
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(
                        imageVector = if (currentUser != null) Icons.Default.Person else Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(32.dp),
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (currentUser != null) {
                            stringResource(R.string.usuario_conectado, currentUser?.email ?: "")
                        } else {
                            stringResource(R.string.sesion_no_iniciada)
                        },
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }

                // TextButton para permitir el inicio de sesión o la finalización de la misma
                TextButton(
                    onClick = {
                        if (currentUser != null) {
                            authViewModel.logout()
                        } else {
                            navController.navigate(RutasNavegacion.InicioSesion.route)
                        }
                    },
                ) {
                    Text(
                        text = if (currentUser != null) {
                            stringResource(R.string.cerrar_sesion)
                        } else {
                            stringResource(R.string.iniciar_sesion_boton)
                        },
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Opción: Autenticación / Inicio de Sesión
        MenuItemCard(
            title = if (currentUser != null) stringResource(R.string.inicio_sesion) else stringResource(R.string.inicio_sesion_titulo),
            icon = Icons.Default.AccountCircle,
            onClick = { navController.navigate(RutasNavegacion.InicioSesion.route) },
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Opción: Configuración -> Navega a la ruta "configuracion"
        MenuItemCard(
            title = stringResource(R.string.configuracion),
            icon = Icons.Default.Settings,
            onClick = { navController.navigate(RutasNavegacion.Configuracion.route) },
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Opción: Acerca De -> Navega a la ruta "acerca_de"
        MenuItemCard(
            title = stringResource(R.string.acerca_de),
            icon = Icons.Default.Info,
            onClick = { navController.navigate(RutasNavegacion.AcercaDe.route) },
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Opción: Ayuda -> Navega a la ruta "ayuda"
        MenuItemCard(
            title = stringResource(R.string.ayuda),
            icon = Icons.AutoMirrored.Filled.Help,
            onClick = { navController.navigate(RutasNavegacion.Ayuda.route) },
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Opción: Historia -> Navega a la ruta "historia"
        MenuItemCard(
            title = stringResource(R.string.historia),
            icon = Icons.Default.History,
            onClick = { navController.navigate(RutasNavegacion.Historia.route) },
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Opción: Sobre Nosotros -> Navega a la ruta "sobre_nosotros"
        MenuItemCard(
            title = stringResource(R.string.sobre_nosotros),
            icon = Icons.Default.Group,
            onClick = { navController.navigate(RutasNavegacion.SobreNosotros.route) },
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Opción: Pokédex -> Navega a la ruta "pokedex"
        MenuItemCard(
            title = stringResource(R.string.pokedex),
            icon = Icons.Default.CatchingPokemon,
            onClick = { navController.navigate(RutasNavegacion.Pokedex.route) },
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Opción: Salir de la aplicación -> Abre diálogo de confirmación
        MenuItemCard(
            title = stringResource(R.string.salir_aplicacion),
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            onClick = { showExitDialog = true },
            isDestructive = true,
        )
    }

    // Diálogo de confirmación (AlertDialog) para salir de la aplicación
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.dialog_salir_titulo),
                    fontWeight = FontWeight.Bold,
                )
            },
            text = {
                Text(text = stringResource(R.string.dialog_salir_mensaje))
            },
            confirmButton = {
                Button(
                    onClick = {
                        showExitDialog = false
                        // Cierra la actividad de la aplicación de forma limpia
                        (context as? Activity)?.finish()
                    },
                ) {
                    Text(text = stringResource(R.string.confirmar))
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showExitDialog = false },
                ) {
                    Text(text = stringResource(R.string.cancelar))
                }
            },
        )
    }
}

/**
 * Componible auxiliar que genera un elemento de menú navegable en forma de [ElevatedCard].
 *
 * @param title Texto que se muestra en la tarjeta.
 * @param icon Icono descriptivo.
 * @param onClick Acción a ejecutar al pulsar la tarjeta.
 * @param isDestructive Si es verdadero, resalta visualmente la opción como destructiva (e.g., Salir).
 */
@Composable
private fun MenuItemCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    isDestructive: Boolean = false,
) {
    val cardColor = if (isDestructive) {
        MaterialTheme.colorScheme.errorContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainerHigh
    }

    val textColor = if (isDestructive) {
        MaterialTheme.colorScheme.onErrorContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.elevatedCardColors(
            containerColor = cardColor,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = textColor,
                modifier = Modifier.size(28.dp),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
            )
        }
    }
}
