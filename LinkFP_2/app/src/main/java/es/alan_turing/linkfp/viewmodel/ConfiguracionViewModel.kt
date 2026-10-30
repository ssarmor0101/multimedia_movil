package es.alan_turing.linkfp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import es.alan_turing.linkfp.model.ConfiguracionData
import es.alan_turing.linkfp.model.ConfiguracionDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ConfiguracionViewModel(
    private val dataStore: ConfiguracionDataStore,
) : ViewModel() {

    // Constructor secundario para inicializar fácilmente con un Context (e.g. desde LocalContext o Application)
    constructor(context: Context) : this(ConfiguracionDataStore(context.applicationContext))

    // Estado observable que contiene tanto clave como valor
    val uiState: StateFlow<ConfiguracionData> = dataStore.preferenceFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ConfiguracionData()
        )

    fun saveClave(clave: String) {
        viewModelScope.launch {
            dataStore.saveClaveToPreferencesStore(clave)
        }
    }

    fun saveValor(valor: String) {
        viewModelScope.launch {
            dataStore.saveValorToPreferencesStore(valor)
        }
    }

    fun saveConfiguracion(clave: String, valor: String) {
        viewModelScope.launch {
            dataStore.saveConfiguracionToPreferencesStore(clave, valor)
        }
    }

    // Alias en español
    fun guardarClave(clave: String) = saveClave(clave)
    fun guardarValor(valor: String) = saveValor(valor)
    fun guardarConfiguracion(clave: String, valor: String) = saveConfiguracion(clave, valor)

    companion object {
        fun provideFactory(
            dataStore: ConfiguracionDataStore
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ConfiguracionViewModel::class.java)) {
                    return ConfiguracionViewModel(dataStore) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }

        fun provideFactory(
            context: Context
        ): ViewModelProvider.Factory = provideFactory(ConfiguracionDataStore(context.applicationContext))
    }
}

class ConfiguracionViewModelFactory(
    private val dataStore: ConfiguracionDataStore
) : ViewModelProvider.Factory {

    constructor(context: Context) : this(ConfiguracionDataStore(context.applicationContext))

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConfiguracionViewModel::class.java)) {
            return ConfiguracionViewModel(dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
