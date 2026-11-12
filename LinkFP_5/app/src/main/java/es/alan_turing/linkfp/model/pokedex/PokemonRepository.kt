package es.alan_turing.linkfp.model.pokedex

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import es.alan_turing.linkfp.R
import es.alan_turing.linkfp.model.RetrofitInstance

/**
 * ==============================================================================================
 * ¿QUÉ HACE EL PATRÓN REPOSITORIO (`PokemonRepository`) Y CÓMO GESTIONA EL FALLBACK LOCAL?
 * ==============================================================================================
 *
 * 1. ¿QUÉ HACE EL REPOSITORIO?:
 *    El patrón Repositorio actúa como un intermediario o "fuente única de la verdad" entre los orígenes
 *    de datos (red, base de datos local, archivos de recursos) y la capa de presentación (ViewModel).
 *    Aísla la lógica de obtención de datos para que el ViewModel no sepa ni le importe de dónde vienen.
 *
 * 2. ¿CÓMO FUNCIONA EL MANEJO DE ERRORES Y FALLBACK?:
 *    - Primero intenta consumir los datos de Internet utilizando Retrofit 2 ([ClientePokemon]).
 *    - Si la petición tiene éxito, retorna la lista de Pokémon remota.
 *    - Si la red falla (falta de conexión a Internet, servidor caído, timeout, etc.), se captura la
 *      excepción en el bloque `try-catch` y se ejecuta `cargarDesdeRawLocal()`.
 *    - `cargarDesdeRawLocal()` abre y lee el recurso estático `res/raw/pokedex_simple.json` usando
 *      Gson, garantizando que la aplicación continúe funcionando perfectamente sin cerrarse.
 * ==============================================================================================
 *
 * @param clientePokemon Cliente de red inyectado o por defecto [es.alan_turing.linkfp.model.RetrofitInstance.api].
 */
class PokemonRepository(
    private val clientePokemon: ClientePokemon = RetrofitInstance.api
) {
    /**
     * Obtiene la lista de Pokémon solicitándola a la API con Retrofit 2.
     * En caso de error de red, carga y parsea el archivo local 'pokedex_simple.json'.
     *
     * @param context Contexto de Android necesario para leer el recurso 'raw' en caso de error.
     * @return Lista de objetos [DatosPokemon].
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
     * Lee el recurso crudo 'res/raw/pokedex_simple.json' y lo convierte en lista de objetos con Gson.
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
