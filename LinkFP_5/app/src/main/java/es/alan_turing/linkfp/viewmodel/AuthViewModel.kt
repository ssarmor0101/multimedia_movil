package es.alan_turing.linkfp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ==============================================================================================
 * GUÍA EDUCATIVA Y DOCUMENTACIÓN DE AuthViewModel (AUTENTICACIÓN CON FIREBASE Y MVVM)
 * ==============================================================================================
 *
 * 1. ¿QUÉ HACE ESTA CLASE?:
 *    `AuthViewModel` es la clase encargada de gestionar el estado de la autenticación de los usuarios
 *    en la aplicación (Inicio de Sesión, Registro y Cierre de Sesión). Actúa como intermediaria
 *    entre el servicio remoto de Firebase Authentication y la capa de UI (Jetpack Compose).
 *
 * 2. PATRÓN MVVM (Model-View-ViewModel) SIN INYECCIÓN DE DEPENDENCIAS (Hilt):
 *    - **Model**: [FirebaseAuth] gestiona los datos de los usuarios en la nube.
 *    - **ViewModel**: Mantiene el estado ([AuthState], [FirebaseUser]) ante cambios de configuración
 *      (como rotaciones de pantalla) y expone métodos para autenticación.
 *    - **View**: Las funciones componibles ([es.alan_turing.linkfp.ui.auth.InicioSesion] y
 *      [es.alan_turing.linkfp.ui.auth.Registro]) observan los [StateFlow] expuestos por este ViewModel.
 *    - En este proyecto **NO se utiliza Dagger Hilt** para inyección de dependencias. En su lugar,
 *      obtenemos la instancia directa de [FirebaseAuth.getInstance] dentro de la propia ViewModel
 *      o permitimos pasarla en el constructor opcionalmente.
 *
 * 3. FUNCIONAMIENTO DE FIREBASE AUTHENTICATION:
 *    - Firebase administra las sesiones automáticamente. Cuando un usuario inicia sesión, Firebase
 *      guarda el token de autenticación localmente en el dispositivo. Al reabrir la app,
 *      `auth.currentUser` recuperará al usuario activo automáticamente sin necesidad de reautenticar.
 *    - `auth.addAuthStateListener` permite escuchar cambios de estado de sesión en tiempo real.
 *
 * ==============================================================================================
 */

/**
 * Representa los diferentes estados en los que se puede encontrar la UI durante el proceso de autenticación.
 */
sealed class AuthState {
    /** Estado inicial o en reposo, sin ninguna operación pendiente ni error. */
    data object Idle : AuthState()

    /** Estado activo durante la llamada asíncrona a Firebase Auth (mostrando barra de progreso). */
    data object Loading : AuthState()

    /** Estado exitoso tras completar el inicio de sesión o registro correctamente. */
    data class Success(val message: String) : AuthState()

    /** Estado de error tras fallar la autenticación (e.g. credenciales incorrectas, usuario existente). */
    data class Error(val message: String) : AuthState()
}

/**
 * ViewModel que gestiona la autenticación con Firebase.
 *
 * @param auth Instancia de [FirebaseAuth] (por defecto [FirebaseAuth.getInstance]).
 */
class AuthViewModel(
    // TODO: En un proyecto en producción, se recomienda inyectar la instancia mediante Hilt o Koin.
    // Aquí inicializamos FirebaseAuth directamente para cumplir con el requisito sin Dagger Hilt.
    private val auth: FirebaseAuth = try {
        FirebaseAuth.getInstance()
    } catch (_: Exception) {
        FirebaseAuth.getInstance()
    }
) : ViewModel() {

    // Estado reactivo que contiene el usuario actual conectado (o null si no hay sesión activa)
    private val _currentUser = MutableStateFlow(auth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    // Estado reactivo para notificar a la UI sobre progresos, éxitos o errores
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        // Escuchador en tiempo real de Firebase para actualizar el estado del usuario cuando inicie/cierre sesión
        // TODO: Implementar configuración completa de Firebase Console (https://console.firebase.google.com/)
        try {
            auth.addAuthStateListener { firebaseAuth ->
                _currentUser.value = firebaseAuth.currentUser
            }
        } catch (e: Exception) {
            // Manejo preventivo para entornos de prueba
        }
    }

    /**
     * Inicia sesión con correo electrónico y contraseña utilizando Firebase Auth.
     *
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @param onSuccess Callback ejecutado tras un inicio de sesión exitoso.
     * @param onError Callback ejecutado con el mensaje de error si falla la autenticación.
     */
    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        if (email.isBlank() || password.isBlank()) {
            val errorMsg = "El correo y la contraseña no pueden estar vacíos"
            _authState.value = AuthState.Error(errorMsg)
            onError(errorMsg)
            return
        }

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            try {
                // TODO: Llamada principal a la API de Firebase Authentication para validar credenciales.
                auth.signInWithEmailAndPassword(email.trim(), password.trim())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            _currentUser.value = user
                            _authState.value = AuthState.Success("Sesión iniciada correctamente")
                            onSuccess()
                        } else {
                            val error = task.exception?.localizedMessage
                                ?: "Error al iniciar sesión. Verifique sus credenciales."
                            _authState.value = AuthState.Error(error)
                            onError(error)
                        }
                    }
            } catch (e: Exception) {
                // TODO: Si el proyecto Firebase no está configurado en la consola de Google, capturamos la excepción genérica
                val errorMsg = e.localizedMessage ?: "Error de comunicación con Firebase Auth"
                _authState.value = AuthState.Error(errorMsg)
                onError(errorMsg)
            }
        }
    }

    /**
     * Registra un nuevo usuario con correo electrónico y contraseña en Firebase Auth.
     *
     * @param email Correo electrónico del nuevo usuario.
     * @param password Contraseña elegida.
     * @param onSuccess Callback ejecutado tras completar el registro de forma exitosa.
     * @param onError Callback ejecutado si el registro falla (e.g., correo ya registrado).
     */
    fun signUp(
        email: String,
        password: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        if (email.isBlank() || password.isBlank()) {
            val errorMsg = "Por favor, completa todos los campos"
            _authState.value = AuthState.Error(errorMsg)
            onError(errorMsg)
            return
        }

        if (password.length < 6) {
            val errorMsg = "La contraseña debe tener al menos 6 caracteres"
            _authState.value = AuthState.Error(errorMsg)
            onError(errorMsg)
            return
        }

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            try {
                // TODO: Llamada principal a la API de Firebase Authentication para crear un usuario nuevo.
                auth.createUserWithEmailAndPassword(email.trim(), password.trim())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            _currentUser.value = user
                            _authState.value = AuthState.Success("Usuario registrado exitosamente")
                            onSuccess()
                        } else {
                            val error = task.exception?.localizedMessage
                                ?: "Error al crear la cuenta. Intente de nuevo."
                            _authState.value = AuthState.Error(error)
                            onError(error)
                        }
                    }
            } catch (e: Exception) {
                // TODO: Captura de excepción si Firebase no ha sido habilitado en la nube aún.
                val errorMsg = e.localizedMessage ?: "Error de conexión con servicio de Firebase"
                _authState.value = AuthState.Error(errorMsg)
                onError(errorMsg)
            }
        }
    }

    /**
     * Cierra la sesión activa del usuario actual en Firebase Auth.
     */
    fun logout() {
        try {
            // TODO: Llamada a Firebase Auth para eliminar el token de usuario del almacenamiento local.
            auth.signOut()
            _currentUser.value = null
            _authState.value = AuthState.Idle
        } catch (e: Exception) {
            _currentUser.value = null
            _authState.value = AuthState.Idle
        }
    }

    /**
     * Reinicia el estado de autenticación a [AuthState.Idle].
     */
    fun clearError() {
        _authState.value = AuthState.Idle
    }
}
