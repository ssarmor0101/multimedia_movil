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
 * CÓMO FUNCIONA LA PANTALLA DE REGISTRO (Registro.kt)
 * ==============================================================================================
 *
 * 1. ¿QUÉ HACE ESTE COMPONENTE?:
 *    Proporciona un formulario de alta de usuario con correo, contraseña y confirmación de contraseña,
 *    incluyendo validaciones locales previas al envío a Firebase Authentication.
 *
 * 2. FUNCIONAMIENTO PEDAGÓGICO:
 *    - **Validación Local previa**: Antes de llamar a Firebase, comprobamos que los campos no estén vacíos,
 *      que las contraseñas coincidan y que la clave tenga al menos 6 caracteres.
 *    - **Control de estado visual**: Mantiene variables locales con `remember` para alternar la visibilidad
 *      de las contraseñas e informar de errores en pantalla.
 *    - **Integración con AuthViewModel**: Llama al método `authViewModel.signUp(...)` y reacciona a los
 *      cambios de estado mediante `AuthState`.
 *    - **Navegación limpia**: Al completar el registro con éxito, redirige al usuario a la pantalla
 *      principal sustituyendo la pila de retroceso.
 * ==============================================================================================
 *
 * @param navController Controlador de navegación expuesto para redirigir entre pantallas.
 * @param modifier Modificador visual opcional de Compose.
 * @param authViewModel Instancia del ViewModel de autenticación.
 */
@Composable
fun Registro(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel(),
) {
    val context = LocalContext.current

    // Estados locales para los campos del formulario
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    var localError by remember { mutableStateOf<String?>(null) }

    // Estado reactivo del ViewModel
    val authState by authViewModel.authState.collectAsState()

    // Cadenas traducidas para Toasts y mensajes de validación
    val camposVaciosMsg = stringResource(R.string.error_campos_vacios)
    val contraseniasNoCoincidenMsg = stringResource(R.string.error_contrasenias_no_coinciden)
    val contraseniaCortaMsg = stringResource(R.string.error_contrasenia_corta)
    val toastExitoRegistroMsg = stringResource(R.string.auth_exito_registro)

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
            text = stringResource(R.string.registro_titulo),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtítulo descriptivo
        Text(
            text = stringResource(R.string.registro_subtitulo),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Tarjeta contenedora del formulario de registro
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

                Spacer(modifier = Modifier.height(14.dp))

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
                        imeAction = ImeAction.Next,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Campo: Confirmar Contraseña
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        localError = null
                        authViewModel.clearError()
                    },
                    label = { Text(text = stringResource(R.string.confirm_password_label)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = stringResource(R.string.confirm_password_label),
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                            Icon(
                                imageVector = if (isConfirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (isConfirmPasswordVisible) {
                                    stringResource(R.string.ocultar_contrasenia)
                                } else {
                                    stringResource(R.string.mostrar_contrasenia)
                                },
                            )
                        }
                    },
                    visualTransformation = if (isConfirmPasswordVisible) {
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

                Spacer(modifier = Modifier.height(20.dp))

                // Muestra de errores en pantalla
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

                // Botón principal de Registro
                Button(
                    onClick = {
                        when {
                            email.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                                localError = camposVaciosMsg
                            }
                            password != confirmPassword -> {
                                localError = contraseniasNoCoincidenMsg
                            }
                            password.length < 6 -> {
                                localError = contraseniaCortaMsg
                            }
                            else -> {
                                authViewModel.signUp(
                                    email = email,
                                    password = password,
                                    onSuccess = {
                                        Toast.makeText(context, toastExitoRegistroMsg, Toast.LENGTH_SHORT).show()
                                        navController.navigate(RutasNavegacion.Principal.route) {
                                            popUpTo(RutasNavegacion.Principal.route) { inclusive = true }
                                        }
                                    },
                                    onError = { err ->
                                        localError = err
                                    },
                                )
                            }
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
                        text = stringResource(R.string.registrarse_boton),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Enlace para ir a la pantalla de Inicio de Sesión
        TextButton(
            onClick = {
                authViewModel.clearError()
                navController.navigate(RutasNavegacion.InicioSesion.route)
            },
        ) {
            Text(
                text = stringResource(R.string.ir_a_inicio_sesion),
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
