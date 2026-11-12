package es.alan_turing.linkfp.model.pokedex

import com.google.gson.annotations.SerializedName

/**
 * ==============================================================================================
 * ¿QUÉ ES UN OBJETO WRAPPER O ENVOLTURA JSON?
 * ==============================================================================================
 *
 * 1. ¿QUÉ HACE ESTA CLASE?:
 *    Muchas APIs REST no devuelven una lista directamente en la raíz del documento JSON, sino un
 *    objeto raíz que contiene una propiedad envolvente (en este caso, `"pokemon": [...]`).
 *
 * 2. ¿CÓMO FUNCIONA EL DESERIALIZADOR?:
 *    Gson mapea la propiedad `"pokemon"` del JSON a la propiedad `val pokemon: List<DatosPokemon>`
 *    de esta clase, extrayendo la colección completa de elementos.
 * ==============================================================================================
 */
data class PokedexResponse(
    @SerializedName("pokemon") val pokemon: List<DatosPokemon> = emptyList()
)
