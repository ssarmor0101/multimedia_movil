package es.alan_turing.linkfp.viewmodel

import androidx.lifecycle.ViewModel

data class ConfiguracionData(
    val clave: String = "default",
    val valor: String = "default"
)

class ConfiguracionViewModel: ViewModel() {
    
}