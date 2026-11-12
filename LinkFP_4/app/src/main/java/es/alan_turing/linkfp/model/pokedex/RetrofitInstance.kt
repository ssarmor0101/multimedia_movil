package es.alan_turing.linkfp.model.pokedex

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Objeto Singleton que gestiona la instancia de Retrofit 2 y proporciona
 * el servicio [ClientePokemon] para realizar las peticiones en red.
 */
object RetrofitInstance {
    private const val BASE_URL = "https://raw.githubusercontent.com/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ClientePokemon by lazy {
        retrofit.create(ClientePokemon::class.java)
    }
}
