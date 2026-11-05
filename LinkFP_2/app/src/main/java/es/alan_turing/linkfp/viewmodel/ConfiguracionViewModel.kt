package es.alan_turing.linkfp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import es.alan_turing.linkfp.model.ConfiguracionDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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
 * Interactúa directamente con [ConfiguracionDataStore] para consultar y guardar las preferencias.
 * 
 * Al no usar inyección de dependencias, proporciona la clase anidada [Factory] para su instanciación
 * mediante el ViewModelProvider en Compose o en la Activity.
 *
 * @property dataStore Instancia del almacén de preferencias de la aplicación.
 */
class ConfiguracionViewModel(
    private val dataStore: ConfiguracionDataStore
) : ViewModel() {

    /**
     * Estado UI expuesto como un [StateFlow] de solo lectura.
     * Combina en tiempo real los flujos [ConfiguracionDataStore.checkBox], [ConfiguracionDataStore.radio]
     * y [ConfiguracionDataStore.dropdown] expuestos por el DataStore y los emite encapsulados en [ConfiguracionData].
     */
    val uiState: StateFlow<ConfiguracionData> = combine(
        dataStore.checkBox,
        dataStore.radio,
        dataStore.dropdown
    ) { checkbox, radio, dropdown ->
        ConfiguracionData(checkbox = checkbox, radio = radio, dropdown = dropdown)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ConfiguracionData()
    )

    /**
     * Modifica y persiste la preferencia del CheckBox en el DataStore de forma asíncrona.
     *
     * @param valor Nuevo estado booleano para el CheckBox.
     */
    fun updateCheckBox(valor: Boolean) {
        viewModelScope.launch {
            dataStore.guardarCheckBox(valor)
        }
    }

    /**
     * Modifica y persiste la preferencia de la opción Radio en el DataStore de forma asíncrona.
     *
     * @param valor Nuevo valor de texto para el botón de radio.
     */
    fun updateRadio(valor: String) {
        viewModelScope.launch {
            dataStore.guardarRadio(valor)
        }
    }

    /**
     * Modifica y persiste la preferencia del menú desplegable (Dropdown) en el DataStore de forma asíncrona.
     *
     * @param valor Nuevo valor de texto para la opción elegida en el menú desplegable.
     */
    fun updateDropdown(valor: String) {
        viewModelScope.launch {
            dataStore.guardarDropdown(valor)
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
