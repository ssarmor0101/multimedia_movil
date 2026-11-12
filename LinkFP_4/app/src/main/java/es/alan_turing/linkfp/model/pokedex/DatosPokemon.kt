package es.alan_turing.linkfp.model.pokedex

import com.google.gson.annotations.SerializedName

/**
 * Data class que representa los datos de un Pokémon obtenidos desde la API o archivo JSON.
 *
 * @param id Identificador único del Pokémon.
 * @param num Número formateado de 3 dígitos (ej: "001").
 * @param name Nombre del Pokémon.
 * @param img URL de la imagen del Pokémon.
 * @param type Lista de tipos a los que pertenece (ej: ["Grass", "Poison"]).
 * @param height Altura del Pokémon.
 * @param weight Peso del Pokémon.
 * @param candy Tipo de caramelo del Pokémon.
 * @param egg Distancia de eclosión de huevo.
 * @param weaknesses Lista de debilidades del Pokémon.
 */
data class DatosPokemon(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("num") val num: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("img") val img: String = "",
    @SerializedName("type") val type: List<String>? = emptyList(),
    @SerializedName("height") val height: String? = null,
    @SerializedName("weight") val weight: String? = null,
    @SerializedName("candy") val candy: String? = null,
    @SerializedName("egg") val egg: String? = null,
    @SerializedName("weaknesses") val weaknesses: List<String>? = emptyList()
)
