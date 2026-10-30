package es.alan_turing.linkfp.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import es.alan_turing.linkfp.model.ConfiguracionDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ConfiguracionData(
    val ckeckbox: Boolean = false,
    val radio: String = "default"
)

class ConfiguracionViewModel(
    private val dataStore: ConfiguracionDataStore,
) : ViewModel() {
    // Aquí se pondrán las variables de estado del state hosting
//    var checkbox: StateFlow<Boolean> = dataStore.checkBox.stateIn()

//    var checkbox = mutableStateOf(false)
//    var checkbox = remember { mutableStateOf(false) }

    // Expose screen UI state
    private val _checkbox = MutableStateFlow(ConfiguracionData())
    val uiState: StateFlow<ConfiguracionData> = _checkbox.asStateFlow()

    init {
        loadConfiguration()
    }

    fun loadConfiguration() {
        checkbox =
    }
}