package es.alan_turing.linkfp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import es.alan_turing.linkfp.model.ConfiguracionDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Estado UI que representa los datos de configuración de la aplicación.
 *
 * @property checkbox Indica el estado de la casilla de verificación (activada/desactivada).
 * @property radio Representa la opción seleccionada dentro del grupo de opciones de radio.
 * @property dropdown Representa la opción seleccionada dentro del menú desplegable.
 */
data class ConfiguracionData(
    val checkbox: Boolean = false,
    val radio: String = "default",
    val dropdown: String = "Opción A"
)

/**
 * ViewModel encargado de la lógica de negocio y gestión del estado UI para la pantalla de Configuración.
 * Mantiene un estado local en memoria y solo persiste los datos en [ConfiguracionDataStore] cuando el
 * usuario pulsa explícitamente el botón de guardar.
 *
 * Al no usar inyección de dependencias, proporciona la clase anidada [Factory] para su instanciación
 * mediante el ViewModelProvider en Compose o en la Activity.
 *
 * @property dataStore Instancia del almacén de preferencias de la aplicación.
 */
class ConfiguracionViewModel(
    private val dataStore: ConfiguracionDataStore
) : ViewModel() {

    // Estado UI mutable privado
    private val _uiState = MutableStateFlow(ConfiguracionData())

    /**
     * Estado UI expuesto como un [StateFlow] de solo lectura para la vista.
     */
    val uiState: StateFlow<ConfiguracionData> = _uiState.asStateFlow()

    init {
        // Carga inicial de datos guardados en DataStore hacia el estado UI en memoria
        viewModelScope.launch {
            combine(
                dataStore.checkBox,
                dataStore.radio,
                dataStore.dropdown
            ) { checkbox, radio, dropdown ->
                ConfiguracionData(checkbox = checkbox, radio = radio, dropdown = dropdown)
            }.first { datos ->
                _uiState.value = datos
                true
            }
        }
    }

    /**
     * Modifica el estado en memoria para la casilla de verificación (CheckBox) sin guardar en DataStore.
     *
     * @param valor Nuevo estado booleano para el CheckBox.
     */
    fun updateCheckBox(valor: Boolean) {
        _uiState.update { it.copy(checkbox = valor) }
    }

    /**
     * Modifica el estado en memoria para la opción Radio sin guardar en DataStore.
     *
     * @param valor Nuevo valor de texto para el botón de radio.
     */
    fun updateRadio(valor: String) {
        _uiState.update { it.copy(radio = valor) }
    }

    /**
     * Modifica el estado en memoria para el menú desplegable (Dropdown) sin guardar en DataStore.
     *
     * @param valor Nuevo valor de texto para la opción elegida en el menú desplegable.
     */
    fun updateDropdown(valor: String) {
        _uiState.update { it.copy(dropdown = valor) }
    }

    /**
     * Persiste de forma asíncrona todos los valores actuales del estado UI en el [ConfiguracionDataStore]
     * al ser llamado mediante la acción del botón de guardar.
     */
    fun guardarConfiguracion() {
        viewModelScope.launch {
            val datosActuales = _uiState.value
            dataStore.guardarCheckBox(datosActuales.checkbox)
            dataStore.guardarRadio(datosActuales.radio)
            dataStore.guardarDropdown(datosActuales.dropdown)
        }
    }

    /**
     * Factoría ([ViewModelProvider.Factory]) requerida para instanciar el [ConfiguracionViewModel]
     * pasando la dependencia de [ConfiguracionDataStore] sin usar Inyección de Dependencias.
     *
     * @property dataStore Instancia de [ConfiguracionDataStore] necesaria para la creación del ViewModel.
     */
    class Factory(private val dataStore: ConfiguracionDataStore) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ConfiguracionViewModel::class.java)) {
                return ConfiguracionViewModel(dataStore) as T
            }
            throw IllegalArgumentException("Clase ViewModel desconocida: ${modelClass.name}")
        }
    }
}

