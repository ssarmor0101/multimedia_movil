package es.alan_turing.linkfp.model

/**
 * ==============================================================================================
 * ¿QUÉ HACE ESTA CLASE Y CÓMO FUNCIONA LA DEFINICIÓN DE RUTAS?
 * ==============================================================================================
 *
 * 1. ¿QUÉ HACE?:
 *    Esta 'sealed class' (clase sellada) define de forma centralizada todas las rutas o destinos de
 *    pantalla disponibles en nuestra aplicación Android con Jetpack Compose.
 *
 * 2. ¿CÓMO FUNCIONA? (Fundamentos pedagógicos):
 *    - En Jetpack Compose, las rutas de navegación son identificadores en forma de cadenas de texto
 *      (String) similares a las URLs de una página web (por ejemplo, "principal", "configuracion").
 *    - Una 'sealed class' en Kotlin nos permite crear una jerarquía cerrada de tipos. Al definir
 *      nuestras rutas con 'data object' dentro de una 'sealed class', conseguimos:
 *        a) SEGURIDAD DE TIPOS (Type-Safety): Evitamos errores tipográficos de escribir "configuracion"
 *           en un sitio y "config" en otro.
 *        b) CENTRALIZACIÓN: Todas las pantallas posibles están agrupadas en este único fichero.
 *        c) ACCESO FACILITADO: Podemos acceder a cualquier ruta como `RutasNavegacion.Principal.route`.
 * ==============================================================================================
 */
sealed class RutasNavegacion(val route: String) {
    /** Ruta de la pantalla principal con el menú de tarjetas */
    data object Principal : RutasNavegacion("principal")

    /** Ruta de la pantalla de configuración y preferencias de usuario */
    data object Configuracion : RutasNavegacion("configuracion")

    /** Ruta de la pantalla que muestra la información sobre la aplicación y autores */
    data object AcercaDe : RutasNavegacion("acerca_de")

    /** Ruta de la pantalla de ayuda al usuario */
    data object Ayuda : RutasNavegacion("ayuda")

    /** Ruta de la pantalla con la historia de la aplicación */
    data object Historia : RutasNavegacion("historia")

    /** Ruta de la pantalla con información detallada de nuestro equipo */
    data object SobreNosotros : RutasNavegacion("sobre_nosotros")
}
