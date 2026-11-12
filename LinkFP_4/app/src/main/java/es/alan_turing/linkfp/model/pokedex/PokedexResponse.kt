package es.alan_turing.linkfp.model.pokedex

import com.google.gson.annotations.SerializedName

/**
 * Data class envolvente para la respuesta JSON de la API de Pokédex
 * que contiene la lista de Pokémon bajo el campo "pokemon".
 */
data class PokedexResponse(
    @SerializedName("pokemon") val pokemon: List<DatosPokemon> = emptyList()
)
