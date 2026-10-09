package com.example.ud1_aplicacion_sencilla.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ud1_aplicacion_sencilla.R
import com.example.ud1_aplicacion_sencilla.ui.components.BuildButton
import com.example.ud1_aplicacion_sencilla.ui.components.BuildCard
import com.example.ud1_aplicacion_sencilla.ui.components.BuildCarousel
import com.example.ud1_aplicacion_sencilla.ui.components.BuildCarouselListaFija
import androidx.compose.material3.Scaffold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Surface
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme


private data class Videojuego(val nombre: String, val categoria: Int);
private val videojuegos = listOf(
    Videojuego("Hollow Knight", R.string.category_metroidvania),
    Videojuego("Super Mario 3D Land", R.string.category_platform),
    Videojuego("The Legend of Zelda", R.string.category_adventure),
    Videojuego("Metal Gear Solid", R.string.category_infiltration),
    Videojuego("Minecraft", R.string.category_survival),
    Videojuego("Sifu", R.string.category_action),
    Videojuego("Pokemon", R.string.category_rpg),
    Videojuego("Rainbow Six Siege", R.string.category_shooter)
);

@Composable
fun  AplicacionSencilla(modifier: Modifier = Modifier) {
    var pulsado by rememberSaveable { mutableStateOf(false) }

    Contenido(
        pulsado = pulsado,
        onPress = { pulsado = true },
        onReset = { pulsado = false },
        modifier = modifier
    )
}

@Composable
fun Contenido(
    pulsado: Boolean,
    onPress: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        BuildCarouselListaFija();
        Spacer(modifier = Modifier.height(20.dp))
        VideogamesCarousel();
        Spacer(modifier = Modifier.height(10.dp))
        Buttons(
            enabled = pulsado,
            onPress = onPress,
            onReset = onReset
        );
    }
}

@Composable
fun VideogamesCarousel() {
    BuildCarousel {
        videojuegos.forEach { videojuego ->
            item {
                BuildCard(modifier = Modifier.size(250.dp)) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            fontSize = 25.sp,
                            text = videojuego.nombre
                        );
                        Row() {
                            Icon(
                                imageVector = Icons.Filled.Category,
                                contentDescription = "Icono"
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                fontSize = 15.sp,
                                text = stringResource(videojuego.categoria)
                            );
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Buttons(
    enabled: Boolean,
    onPress: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    val text = if (!enabled) stringResource(R.string.text_not_change) else stringResource(R.string.text_changed)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = text);
        Row() {
            BuildButton(
                enabled = !enabled,
                onClick = onPress,
                text = stringResource(R.string.press)
            )
            Spacer(Modifier.width(5.dp));
            BuildButton(
                enabled = enabled,
                onClick = onReset,
                text = stringResource(R.string.reset)
            )
        }
    }
}