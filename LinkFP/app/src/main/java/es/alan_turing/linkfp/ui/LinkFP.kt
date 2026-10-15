package es.alan_turing.linkfp.ui

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.alan_turing.linkfp.R
import es.alan_turing.linkfp.SegundaActivity

@Composable
fun Inicio(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            Text(text = stringResource(R.string.hello_world))
            Button(
                onClick = {
                    val intent = Intent(context, SegundaActivity::class.java)
                    context.startActivity(intent)
                },
                shape = MaterialTheme.shapes.small
            ) {
                Text(text = "Si")
            }
            AcercaDe()
            Ayuda()
            Historia()
            SobreNosotros()
        }
    }
}

