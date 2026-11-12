package es.alan_turing.linkfp.ui.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import es.alan_turing.linkfp.viewmodel.AuthViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun InicioSesion(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel = AuthViewModel()
) {
    var email by remember { mutableStateOf("") }

    Column(
        modifier = modifier
    ) {
        Text(
            text="Hello World!"
        )
    }
}