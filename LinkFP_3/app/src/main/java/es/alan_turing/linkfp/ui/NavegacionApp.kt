package es.alan_turing.linkfp.ui

import android.graphics.pdf.content.PdfPageGotoLinkContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import es.alan_turing.linkfp.model.RutasNavegacion

@Composable
fun NavegacionApp(
    navController = navController,
    startDestination: ,
    modifier: Modifier = Modifier
) {
    composable(RutasNavegacion.Principal.route) {
        Principal(navController)
    }

    composable(RutasNavegacion.AcercaDe.route) {
        AcercaDe()
    }
}