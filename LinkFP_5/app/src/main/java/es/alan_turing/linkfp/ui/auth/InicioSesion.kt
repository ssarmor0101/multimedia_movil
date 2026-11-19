package es.alan_turing.linkfp.ui.auth

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import es.alan_turing.linkfp.R
import es.alan_turing.linkfp.model.RutasNavegacion
import es.alan_turing.linkfp.viewmodel.AuthState
import es.alan_turing.linkfp.viewmodel.AuthViewModel

/**
 * ==============================================================================================
 * CÓMO FUNCIONA LA PANTALLA DE INICIO DE SESIÓN (InicioSesion.kt)
 * ==============================================================================================
 *
 * 1. ¿QUÉ HACE ESTE COMPONENTE?:
 *    Ofrece una interfaz gráfica limpia y personalizada para que el usuario pueda introducir su correo
 *    y contraseña e iniciar sesión en Firebase.
 *
 * 2. FUNCIONAMIENTO PEDAGÓGICO:
 *    - **Control de campos de texto**: Usamos `remember { mutableStateOf("") }` para gestionar el estado
 *      local de los inputs (email y password).
 *    - **Visibilidad de contraseña**: Alternamos entre `PasswordVisualTransformation` y `VisualTransformation.None`
 *      mediante un icono en el campo de texto.
 *    - **Consumo de ViewModel sin Hilt**: Recibe la instancia de `AuthViewModel` directamente o
 *      mediante `viewModel()`.
 *    - **Observación de Estado**: Escucha `authViewModel.authState.collectAsState()` para reaccionar
 *      ante estados de carga ([AuthState.Loading]) o errores ([AuthState.Error]).
 *    - **Navegación**: Tras un inicio de sesión exitoso, utiliza `navController.navigate` para volver
 *      a la pantalla principal desapilando las pantallas anteriores.
 * ==============================================================================================
 *
 * @param navController Controlador para gestionar la navegación entre pantallas.
 * @param authViewModel Instancia del ViewModel de autenticación.
 * @param modifier Modificador visual para ajustar insets y márgenes.
 */
@Composable
fun InicioSesion(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel(),
) {
    val context = LocalContext.current

    // Estados locales para los campos del formulario
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(value = false) }
    var localError by remember { mutableStateOf<String?>(null) }

    // Estado reactivo observado del ViewModel
    val authState by authViewModel.authState.collectAsState()

    val camposVaciosMsg = stringResource(R.string.error_campos_vacios)
    val toastExitoMsg = stringResource(R.string.auth_exito_login)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Título principal de la pantalla
        Text(
            text = stringResource(R.string.inicio_sesion_titulo),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtítulo explicativo
        Text(
            text = stringResource(R.string.inicio_sesion_subtitulo),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Tarjeta con los campos del formulario
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Campo: Correo Electrónico
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        localError = null
                        authViewModel.clearError()
                    },
                    label = { Text(text = stringResource(R.string.email_label)) },
                    placeholder = { Text(text = stringResource(R.string.email_hint)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = stringResource(R.string.email_label),
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo: Contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        localError = null
                        authViewModel.clearError()
                    },
                    label = { Text(text = stringResource(R.string.password_label)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = stringResource(R.string.password_label),
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (isPasswordVisible) {
                                    stringResource(R.string.ocultar_contrasenia)
                                } else {
                                    stringResource(R.string.mostrar_contrasenia)
                                },
                            )
                        }
                    },
                    visualTransformation = if (isPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Muestra de mensaje de error si existe
                val mensajeErrorActual = localError ?: (authState as? AuthState.Error)?.message
                AnimatedVisibility(visible = mensajeErrorActual != null) {
                    if (mensajeErrorActual != null) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                        ) {
                            Text(
                                text = mensajeErrorActual,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(12.dp),
                            )
                        }
                    }
                }

                // Botón principal para iniciar sesión
                Button(
                    onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            localError = camposVaciosMsg
                        } else {
                            authViewModel.login(
                                email = email,
                                password = password,
                                onSuccess = {
                                    Toast.makeText(context, toastExitoMsg, Toast.LENGTH_SHORT).show()
                                    navController.navigate(RutasNavegacion.Principal.route) {
                                        popUpTo(RutasNavegacion.Principal.route) { inclusive = true }
                                    }
                                },
                                onError = { err ->
                                    localError = err
                                },
                            )
                        }
                    },
                    enabled = authState !is AuthState.Loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                ) {
                    if (authState is AuthState.Loading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp),
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    Text(
                        text = stringResource(R.string.iniciar_sesion_boton),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Enlace para navegar a la pantalla de registro
        TextButton(
            onClick = {
                authViewModel.clearError()
                navController.navigate(RutasNavegacion.Registro.route)
            },
        ) {
            Text(
                text = stringResource(R.string.ir_a_registro),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón para volver a la pantalla principal
        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth(0.7f),
        ) {
            Text(text = stringResource(R.string.volver))
        }
    }
}
