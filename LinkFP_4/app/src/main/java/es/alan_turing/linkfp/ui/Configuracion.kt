package es.alan_turing.linkfp.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import es.alan_turing.linkfp.R
import es.alan_turing.linkfp.model.ConfiguracionDataStore
import es.alan_turing.linkfp.viewmodel.ConfiguracionViewModel

/**
 * Pantalla de configuración que hace uso de [ConfiguracionViewModel] y [ConfiguracionDataStore]
 * para mostrar y modificar preferencias persistentes.
 *
 * @param navController Controlador de navegación para regresar a la pantalla principal.
 * @param modifier Modificador de estilo para la columna principal.
 */
@Composable
fun Configuracion(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val toastExitoMsg = stringResource(R.string.toast_configuracion_guardada)
    val toastErrorMsg = stringResource(R.string.toast_configuracion_error)

    // Instanciación del ViewModel y DataStore sin frameworks de inyección de dependencias
    val viewModel: ConfiguracionViewModel = remember(context) {
        val dataStore = ConfiguracionDataStore(context)
        val factory = ConfiguracionViewModel.Factory(dataStore)
        if (context is ViewModelStoreOwner) {
            ViewModelProvider(context, factory)[ConfiguracionViewModel::class.java]
        } else {
            ConfiguracionViewModel(dataStore)
        }
    }

    // Recogemos el estado UI emitido por el StateFlow del ViewModel
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(R.string.configuracion),
                fontSize = 40.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Checkbox conectado con ViewModel y DataStore
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Checkbox(
                checked = uiState.checkbox,
                onCheckedChange = { nuevoValor ->
                    viewModel.updateCheckBox(nuevoValor)
                },
            )
            Text(
                text = stringResource(R.string.checkbox_estado, uiState.checkbox),
                fontSize = 18.sp,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // RadioButton conectado con ViewModel y DataStore
        Text(
            text = stringResource(R.string.selecciona_opcion, uiState.radio),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )

        val opcionesRadio = listOf("Opción 1", "Opción 2", "Opción 3")
        opcionesRadio.forEach { opcion ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                RadioButton(
                    selected = (uiState.radio == opcion),
                    onClick = {
                        viewModel.updateRadio(opcion)
                    },
                )
                Text(
                    text = opcion,
                    fontSize = 16.sp,
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // DropdownMenu conectado con ViewModel y DataStore
        Text(
            text = stringResource(R.string.selecciona_desplegable, uiState.dropdown),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )

        var dropdownExpanded by remember { mutableStateOf(false) }
        val opcionesDropdown = listOf("Opción A", "Opción B", "Opción C", "Opción D")

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        ) {
            Button(
                onClick = { dropdownExpanded = true },
            ) {
                Text(text = stringResource(R.string.desplegable_prefix, uiState.dropdown))
            }

            DropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false },
            ) {
                opcionesDropdown.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(text = opcion) },
                        onClick = {
                            viewModel.updateDropdown(opcion)
                            dropdownExpanded = false
                        },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botón para validar y guardar cambios
        Button(
            onClick = {
                val esValido = viewModel.validarYGuardar()
                if (esValido) {
                    Toast.makeText(
                        context,
                        toastExitoMsg,
                        Toast.LENGTH_LONG
                    ).show()
                    navController.popBackStack()
                } else {
                    Toast.makeText(
                        context,
                        toastErrorMsg,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.guardar_cambios),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón para volver a la pantalla principal
        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(R.string.volver))
        }
    }
}
