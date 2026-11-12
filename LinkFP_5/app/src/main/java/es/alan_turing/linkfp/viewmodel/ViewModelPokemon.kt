package es.alan_turing.linkfp.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.alan_turing.linkfp.model.pokedex.DatosPokemon
import es.alan_turing.linkfp.model.pokedex.PokemonRepository
import kotlinx.coroutines.launch

/**
 * ==============================================================================================
 * ¿QUÉ HACE EL VIEWMODEL (`ViewModelPokemon`) Y CÓMO GESTIONA EL ESTADO DE LA UI Y CORRUTINAS?
 * ==============================================================================================
 *
 * 1. ¿QUÉ HACE EL VIEWMODEL?:
 *    El ViewModel preserva y gestiona los datos relacionados con la UI de forma consciente del ciclo
 *    de vida de Android (Lifecycle-Aware). Sobrevive a rotaciones de pantalla y cambios de configuración.
 *
 * 2. ¿CÓMO FUNCIONA EL FLUJO DE DATOS Y ESTADO EN COMPOSE?:
 *
 *    A) ESTADO DE COMPOSE (`mutableStateOf` / `State`):
 *       - Encapsulamos la lista de Pokémon en un estado observable `_listaPokemons`.
 *       - Exponemos una versión de solo lectura `listaPokemons: State<List<DatosPokemon>>` hacia la UI.
 *       - Cuando la lista cambia en el ViewModel, Jetpack Compose recompone automáticamente la pantalla.
 *
 *    B) ÁMBITO DE CORRUTINA (`viewModelScope.launch`):
 *       - `viewModelScope` es un contexto de corrutinas vinculado al ciclo de vida del ViewModel.
 *       - Al lanzar la carga con `viewModelScope.launch`, la petición de red/repositorio se efectúa en
 *         segundo plano de forma asíncrona y, si el usuario abandona la pantalla, la petición se cancela
 *         automáticamente para evitar fugas de memoria.
 * ==============================================================================================
 *
 * @param repository Instancia del repositorio de datos [PokemonRepository].
 */
class ViewModelPokemon(
    private val repository: PokemonRepository = PokemonRepository()
) : ViewModel() {

    /** Estado mutable privado que almacena la lista de Pokémon */
    private val _listaPokemons = mutableStateOf<List<DatosPokemon>>(emptyList())

    /** Estado inmutable público observado por la UI en Compose */
    val listaPokemons: State<List<DatosPokemon>> = _listaPokemons

    /** Estado mutable de carga para controlar el indicador de progreso circular */
    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    /**
     * Inicia el proceso asíncrono en segundo plano para cargar los datos desde el repositorio.
     *
     * @param context Contexto de Android expuesto para el fallback local en caso de error de red.
     */
    fun cargarPokemons(context: Context? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            val resultado = repository.getPokemons(context)
            _listaPokemons.value = resultado
            _isLoading.value = false
        }
    }
}
