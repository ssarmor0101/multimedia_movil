package es.alan_turing.ssarmor0101.mistareas.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import es.alan_turing.ssarmor0101.mistareas.R
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import es.alan_turing.ssarmor0101.mistareas.ui.theme.MisTareasTheme

data class Tarea(
    val id: Int,
    val nombre: String,
    val completada: Boolean = false,
)

@Composable
fun TarjetasTareas(modifier: Modifier = Modifier) {
    val tareasIniciales = listOf(
        Tarea(id = 1, nombre = stringResource(R.string.study_kotlin)),
        Tarea(id = 2, nombre = stringResource(R.string.do_job)),
        Tarea(id = 3, nombre = stringResource(R.string.read_book)),
        Tarea(id = 4, nombre = stringResource(R.string.check_email))
    )

    val listaTareas = remember { tareasIniciales.toMutableStateList() }

    val cambioAlMarcar = {index: Int, nuevoValor: Boolean -> listaTareas[index] = listaTareas[index].copy(completada = nuevoValor) }

    ListarTareas(
        modifier = modifier,
        tareas = listaTareas,
        cambioAlMarcar = cambioAlMarcar
    )
}

@Composable
fun ListarTareas(
    modifier: Modifier,
    tareas: SnapshotStateList<Tarea>,
    cambioAlMarcar: (Int, Boolean) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            style = MaterialTheme.typography.displayMedium,
            text = stringResource(R.string.app_name)
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(
                items=tareas,
                key={ _, tarea -> tarea.id }
            ) { index, tarea ->
                CardTareas(
                    name = tarea.nombre,
                    completada = tarea.completada,
                    cambioAlMarcar = {nuevoValor: Boolean -> cambioAlMarcar(index, nuevoValor)}
                )
            }
        }
    }

}

@Composable
fun CardTareas(
    name: String,
    completada: Boolean,
    cambioAlMarcar: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable{cambioAlMarcar(!completada)},
        colors = CardDefaults.cardColors(
            if (completada) MaterialTheme.colorScheme.primary else Color.Transparent
        ),
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp).background(Color.Transparent),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = if (completada) TextDecoration.LineThrough else TextDecoration.None,
                )
            )
            Checkbox(
                checked = completada,
                onCheckedChange = cambioAlMarcar
            )
        }
    }
}

@Preview(
    name = "Pantalla en español y modo claro",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "es"
)
@Preview(
    name = "Pantalla en español y modo oscuro",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    locale = "es"
)
@Preview(
    name = "Pantalla en ingles y modo claro",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "en"
)
@Preview(
    name = "Pantalla en ingles y modo oscuro",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    locale = "en"
)
@Composable
fun MisTareasPreview() {
    MisTareasTheme(
        dynamicColor = false
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            TarjetasTareas(modifier = Modifier.padding(innerPadding))
        }
    }
}