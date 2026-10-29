package es.alan_turing.linkfp.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import es.alan_turing.linkfp.MainActivity
import es.alan_turing.linkfp.R
import es.alan_turing.linkfp.SegundaActivity
import es.alan_turing.linkfp.ui.theme.LinkFPTheme

@Composable
fun Inicio(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier.padding(10.dp)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 50.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.button_move_to_view, 2)
            )
            Button(
                onClick = {
                    val intent = Intent(context, SegundaActivity::class.java)
                    context.startActivity(intent)
                },
                shape = MaterialTheme.shapes.small
            ) {
                Text(text = stringResource(R.string.button))
            }
        }

        AcercaDe()
        Ayuda()
        Historia()
        SobreNosotros()
    }
}

@Composable
fun Pantalla2(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier.padding(10.dp)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.button_move_to_view, 1)
            )
            Button(
                onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                },
                shape = MaterialTheme.shapes.small
            ) {
                Text(text = stringResource(R.string.button))
            }
        }
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/place/Parque+Tecnol%C3%B3gico+de+Andaluc%C3%ADa,+Campanillas,+29590+M%C3%A1laga/@36.7384356,-4.5621236,15z/data=!3m1!4b1!4m6!3m5!1s0xd72f1cd54795bd7:0x502d7a05bf98b90!8m2!3d36.7356241!4d-4.5495489!16s%2Fm%2F080bnh9?entry=ttu&g_ep=EgoyMDI2MTAyMC4wIKXMDSoASAFQAw%3D%3D"))
                context.startActivity(intent)
            }
        ) {
            Text(text = "YouTube")
        }

    }
}

@Preview
@Composable
fun PreviewInicio() {
    LinkFPTheme(dynamicColor = false) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Inicio(modifier = Modifier.padding(innerPadding));
        }
    }
}

@Preview
@Composable
fun PreviewPantalla2() {
    LinkFPTheme(dynamicColor = false) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Pantalla2(modifier = Modifier.padding(innerPadding));
        }
    }
}