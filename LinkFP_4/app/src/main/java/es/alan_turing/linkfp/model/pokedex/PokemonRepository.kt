package es.alan_turing.linkfp.model.pokedex

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import es.alan_turing.linkfp.R

/**
 * Repositorio de Pokémon encargado de suministrar los datos de la Pokédex.
 * Intenta obtenerlos desde la API remota mediante Retrofit 2 y, si ocurre alguna excepción,
 * recurre a la carga desde el recurso local 'res/raw/pokedex_simple.json'.
 */
class PokemonRepository(
    private val clientePokemon: ClientePokemon = RetrofitInstance.api
) {
    /**
     * Obtiene la lista de Pokémon de la API remota con Retrofit 2.
     * En caso de fallo o ausencia de conexión, carga los datos del archivo local 'pokedex_simple.json'.
     */
    suspend fun getPokemons(context: Context? = null): List<DatosPokemon> {
        return try {
            val response = clientePokemon.getPokemons()
            if (response.pokemon.isNotEmpty()) {
                response.pokemon
            } else {
                cargarDesdeRawLocal(context)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            cargarDesdeRawLocal(context)
        }
    }

    /**
     * Carga y parsea la lista de Pokémon desde 'res/raw/pokedex_simple.json'.
     */
    private fun cargarDesdeRawLocal(context: Context?): List<DatosPokemon> {
        if (context == null) return emptyList()
        return try {
            val inputStream = context.resources.openRawResource(R.raw.pokedex_simple)
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<DatosPokemon>>() {}.type
            Gson().fromJson(jsonString, type) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
