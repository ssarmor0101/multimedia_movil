package es.alan_turing.linkfp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.alan_turing.linkfp.R

@Composable
fun AcercaDe() {
    Column(

    ) {
        Text(
            text = "${stringResource(R.string.acerca_de)} Dani, Celia, Sergio"
        )
        Text(
            text = "${stringResource(R.string.licencia)}: GPL"
        )
        Image(
            painter = painterResource(id = R.drawable.android_robot),
            contentDescription = "Imagen de android",
            modifier = Modifier.size(100.dp)
        )
    }
}