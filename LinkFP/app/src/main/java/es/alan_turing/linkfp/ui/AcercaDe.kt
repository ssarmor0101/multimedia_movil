package es.alan_turing.linkfp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.alan_turing.linkfp.R

@Composable
fun AcercaDe(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.acerca_de)
            )
        }
        Text(
            text = "${stringResource(R.string.acerca_de_texto)} Dani, Celia, Sergio"
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