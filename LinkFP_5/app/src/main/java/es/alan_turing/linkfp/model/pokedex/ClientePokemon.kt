package es.alan_turing.linkfp.model.pokedex

import retrofit2.http.GET

/**
 * ==============================================================================================
 * ¿QUÉ ES UNA INTERFAZ DE RETROFIT Y CÓMO FUNCIONAN SUS ANOTACIONES Y CORRUTINAS?
 * ==============================================================================================
 *
 * 1. ¿QUÉ HACE ESTA INTERFAZ?:
 *    En Retrofit no escribimos la implementación del código de red; únicamente declaramos una
 *    interfaz de Kotlin con los métodos HTTP que deseamos ejecutar. Retrofit genera dinámicamente
 *    la implementación de red en tiempo de ejecución (`retrofit.create()`).
 *
 * 2. ¿CÓMO FUNCIONAN SUS ELEMENTOS? (Explicación para principiantes):
 *
 *    A) ANOTACIÓN `@GET("ruta_relativa")`:
 *       - Indica que la función realizará una petición de lectura HTTP GET.
 *       - La cadena especificada ("Biuni/PokemonGO-Pokedex/master/pokedex.json") es la ruta relativa
 *         que se concatena con la `BASE_URL` definida en [RetrofitInstance].
 *
 *    B) FUNCIONES SUSPENDIDAS (`suspend fun`):
 *       - Las peticiones en red consumen tiempo y NUNCA deben ejecutarse en el hilo principal (Main/UI Thread),
 *         ya que congelarían la interfaz de usuario.
 *       - La palabra clave `suspend` permite ejecutar la petición de forma asíncrona mediante Corrutinas
 *         de Kotlin, pausando la ejecución de la función sin bloquear la pantalla mientras espera la respuesta.
 *
 *    C) TIPO DE RETORNO (`PokedexResponse`):
 *       - Retrofit y Gson leen la respuesta JSON recibida del servidor y deserializan los campos
 *         automáticamente dentro del objeto de datos Kotlin especificado.
 * ==============================================================================================
 */
interface ClientePokemon {
    /**
     * Realiza una petición GET para obtener la lista de Pokémon desde la API en GitHub.
     *
     * @return Objeto [PokedexResponse] deserializado a partir del JSON devuelto por la API.
     */
    @GET("Biuni/PokemonGO-Pokedex/master/pokedex.json")
    suspend fun getPokemons(): PokedexResponse
}
