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
import androidx.compose.material.icons.filled.CatchingPokemon
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavHostController
import es.alan_turing.linkfp.R
import es.alan_turing.linkfp.model.RutasNavegacion

/**
 * ==============================================================================================
 * CÓMO FUNCIONA LA NAVEGACIÓN DESDE LA PANTALLA PRINCIPAL
 * ==============================================================================================
 *
 * 1. ¿QUÉ HACE ESTA PANTALLA?:
 *    Muestra el menú de navegación de la app compuesto por tarjetas [ElevatedCard].
 *    Permite al usuario navegar a cualquiera de las pantallas secundarias o salir de la aplicación.
 *
 * 2. ¿CÓMO FUNCIONA LA NAVEGACIÓN DESDE EVENTOS DE CLIC?:
 *    - Recibe como parámetro `navController: NavHostController`.
 *    - Al hacer clic en cada opción, se invoca `navController.navigate(RutasNavegacion.<DESTINO>.route)`.
 *    - Esta llamada notifica al controlador de navegación que apile (push) la nueva pantalla
 *      en la pila de retroceso (Back Stack) y actualice la UI visible en el `NavHost`.
 *    - Para la opción "Salir", se activa una variable de estado `showExitDialog` para mostrar un [AlertDialog]
 *      que, al ser confirmado, finaliza la actividad actual mediante `(context as? Activity)?.finish()`.
 * ==============================================================================================
 *
 * @param navController Controlador de navegación expuesto para ejecutar `navigate()`.
 * @param modifier Modificador de diseño.
 */
@Composable
fun Principal(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var showExitDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(R.string.menu_principal),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

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
        colors = androidx.compose.material3.CardDefaults.elevatedCardColors(
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
