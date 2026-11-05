package es.alan_turing.linkfp.model

sealed class RutasNavegacion(val route: String) {
    data object Principal: RutasNavegacion("principal")
    data object AcercaDe: RutasNavegacion("acerca_de")
    data object Ayuda: RutasNavegacion("ayuda")
    data object SobreNosotros: RutasNavegacion("sobre_nosotros")
    data object Configuracion: RutasNavegacion("configuracion")
}