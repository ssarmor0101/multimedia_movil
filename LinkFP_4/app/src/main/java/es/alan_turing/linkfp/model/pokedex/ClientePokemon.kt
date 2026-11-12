package es.alan_turing.linkfp.model.pokedex

import retrofit2.http.GET

/**
 * Interfaz de Retrofit 2 para definir los endpoints HTTP de la API de Pokédex.
 */
interface ClientePokemon {
    /**
     * Obtiene la lista de Pokémon desde la API de GitHub de Pokédex.
     */
    @GET("Biuni/PokemonGO-Pokedex/master/pokedex.json")
    suspend fun getPokemons(): PokedexResponse
}
