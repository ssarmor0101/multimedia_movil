package es.alan_turing.linkfp.model.pokedex

import com.google.gson.annotations.SerializedName

/**
 * ==============================================================================================
 * ¿QUÉ ES UNA DATA CLASS DE MODELADO JSON Y CÓMO FUNCIONA `@SerializedName`?
 * ==============================================================================================
 *
 * 1. ¿QUÉ HACE ESTA CLASE?:
 *    Representa la estructura de datos (DTO - Data Transfer Object) de un único Pokémon
 *    mapeado directamente desde el documento JSON de la API.
 *
 * 2. ¿CÓMO FUNCIONA `@SerializedName` DE GSON?:
 *    - La anotación `@SerializedName("nombre_en_json")` vincula cada propiedad de Kotlin con la
 *      clave exacta del archivo JSON recibido del servidor.
 *    - Esto nos permite usar nombres de variables limpios e idiomáticos en Kotlin (ej: `name`)
 *      incluso si la API usa convenciones distintas en JSON (ej: `snake_case` o palabras clave).
 *    - Asignar valores por defecto (ej: `= 0`, `= ""`) y tipos nulables (ej: `List<String>?`)
 *      evita que la aplicación falle (crash por NullPointerException) si la API omite algún dato.
 * ==============================================================================================
 *
 * @param id Identificador numérico único del Pokémon.
 * @param num Número de 3 dígitos (ej: "001").
 * @param name Nombre del Pokémon.
 * @param img URL HTTP de la imagen descriptiva del Pokémon.
 * @param type Lista de tipos (ej: ["Grass", "Poison"]).
 * @param height Altura formateada.
 * @param weight Peso formateado.
 * @param candy Nombre del caramelo asociado.
 * @param egg Distancia de eclosión de huevo.
 * @param weaknesses Lista de debilidades de tipo.
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
