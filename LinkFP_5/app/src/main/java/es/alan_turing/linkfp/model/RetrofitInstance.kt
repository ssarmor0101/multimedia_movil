package es.alan_turing.linkfp.model

import es.alan_turing.linkfp.model.pokedex.ClientePokemon
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * ==============================================================================================
 * ¿QUÉ ES RETROFIT 2 Y CÓMO FUNCIONA LA INSTANCIA CENTRALIZADA (`RetrofitInstance`)?
 * ==============================================================================================
 *
 * 1. ¿QUÉ HACE RETROFIT 2?:
 *    Retrofit es una librería cliente HTTP para Android y Java desarrollada por Square. Convierte
 *    endpoints de APIs HTTP/REST en interfaces orientadas a objetos en Kotlin de forma automática,
 *    eliminando la necesidad de escribir código manual de sockets, HttpURLConnection o parseo directo.
 *
 * 2. ¿CÓMO FUNCIONA ESTE COMPONENTE? (Explicación paso a paso):
 *
 *    A) DIRECCIÓN BASE (`BASE_URL`):
 *       - Retrofit requiere una URL base (ej: "https://raw.githubusercontent.com/") que debe terminar
 *         siempre en una barra final '/'.
 *       - Las rutas relativas definidas en la interfaz ([ClientePokemon]) se concatenarán a esta URL base.
 *
 *    B) FABRICANTE DE CONVERSIÓN (`addConverterFactory`):
 *       - Las APIs en la web responden habitualmente en formato de texto JSON.
 *       - `GsonConverterFactory.create()` integra la librería Gson de Google con Retrofit. Esto permite
 *         transformar de forma transparente la respuesta textual JSON en objetos y colecciones de Kotlin.
 *
 *    C) PATRÓN SINGLETON Y CÁLCULO PEREZOSO (`by lazy`):
 *       - La creación de un cliente de red es costosa en recursos de CPU y memoria.
 *       - Mediante el objeto `object RetrofitInstance` y el delegado `by lazy`, garantizamos que la
 *         instancia de Retrofit y del cliente API se creen solo la primera vez que se necesiten
 *         (Singleton), reutilizándose en todas las peticiones posteriores.
 * ==============================================================================================
 */
object RetrofitInstance {
    /** URL base del servidor web desde donde se consumen los datos */
    private const val BASE_URL = "https://raw.githubusercontent.com/"

    /** Instancia privada de Retrofit configurada con la URL base y el convertidor Gson */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /** Servicio API expuesto para realizar peticiones relativas usando la interfaz [es.alan_turing.linkfp.model.pokedex.ClientePokemon] */
    val api: ClientePokemon by lazy {
        retrofit.create(ClientePokemon::class.java)
    }
}
