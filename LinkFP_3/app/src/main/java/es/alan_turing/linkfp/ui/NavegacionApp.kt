package es.alan_turing.linkfp.ui

import android.graphics.pdf.content.PdfPageGotoLinkContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import es.alan_turing.linkfp.model.RutasNavegacion

@Composable
fun NavegacionApp(
    navController: NavHostController = rememberNavController(),
    startDestination:,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    )
    composable(RutasNavegacion.Principal.route) {
        Principal(navController)
    }

    composable(RutasNavegacion.AcercaDe.route) {
        AcercaDe()
    }
}