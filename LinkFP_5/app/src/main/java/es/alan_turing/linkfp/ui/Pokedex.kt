package es.alan_turing.linkfp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import es.alan_turing.linkfp.R
import es.alan_turing.linkfp.model.pokedex.DatosPokemon
import es.alan_turing.linkfp.viewmodel.ViewModelPokemon

/**
 * ==============================================================================================
 * ¿CÓMO FUNCIONA LA PANTALLA POKÉDEX, LA CARGA ASÍNCRONA Y EL COMPONENTE ASYNCIMAGE DE COIL?
 * ==============================================================================================
 *
 * 1. ¿QUÉ HACE ESTA PANTALLA?:
 *    Es el componible principal de la vista Pokédex. Conecta la capa de UI con el [ViewModelPokemon]
 *    y reacciona a los cambios de estado mostrando un indicador circular de progreso (`CircularProgressIndicator`)
 *    mientras los datos se están descargando desde Retrofit 2, y renderizando un `LazyColumn` cuando
 *    la información está disponible.
 *
 * 2. ¿CÓMO FUNCIONAN SUS COMPONENTES? (Explicación detallada):
 *
 *    A) `LaunchedEffect(Unit)`:
 *       - Ejecuta un bloque de código con corrutinas de manera segura dentro del ciclo de vida del
 *         componible la primera vez que la pantalla entra en la composición.
 *       - Dispara `cargarPokemons()` únicamente si la lista de Pokémon aún está vacía.
 *
 *    B) `LazyColumn` Y `PokedexList`:
 *       - Es el equivalente moderno en Jetpack Compose al `RecyclerView` tradicional de Android.
 *       - Renderiza eficientemente solo los elementos visibles en pantalla, reciclando las vistas
 *         a medida que el usuario realiza desplazamientos (scroll).
 *
 *    C) CARGA ASÍNCRONA DE IMÁGENES CON COIL 3 (`AsyncImage`):
 *       - Las URLs obtenidas del servidor (ej: "http://www.serebii.net/pokemongo/pokemon/001.png")
 *         son procesadas dinámicamente con `pokemon.img.replace("http://", "https://")` para
 *         garantizar la seguridad de la conexión HTTPS.
 *       - `AsyncImage` descarga la imagen de Internet en segundo plano, la almacena en caché en el
 *         dispositivo y muestra un `placeholder` o imagen de error local mientras se completa la descarga.
 * ==============================================================================================
 *
 * @param navController Controlador de navegación para permitir volver atrás con `popBackStack()`.
 * @param viewModelPokemon Instancia del ViewModel que administra la lista de Pokémon y su estado.
 * @param modifier Modificador de diseño.
 */
@Composable
fun Pokedex(
    navController: NavHostController? = null,
    viewModelPokemon: ViewModelPokemon = viewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val listaPokemons by viewModelPokemon.listaPokemons

    // Efecto secundario para disparar la carga asíncrona al abrir la pantalla
    LaunchedEffect(Unit) {
        if (listaPokemons.isEmpty()) {
            viewModelPokemon.cargarPokemons(context)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.pokedex),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Si la lista está vacía, se muestra el indicador circular de progreso (CircularProgressIndicator)
        if (listaPokemons.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.cargando_pokemons),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        } else {
            // Cuando la lista ha cargado sus elementos, invoca a PokedexList
            Box(
                modifier = Modifier.weight(1f)
            ) {
                PokedexList(listaPokemons = listaPokemons)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        navController?.let { nav ->
            OutlinedButton(
                onClick = { nav.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.volver))
            }
        }
    }
}

/**
 * Componente que despliega la lista perezosa (LazyColumn) de Pokémons.
 *
 * @param listaPokemons Lista de datos obtenidos desde la API.
 * @param modifier Modificador de diseño.
 */
@Composable
fun PokedexList(
    listaPokemons: List<DatosPokemon>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(listaPokemons) { pokemon ->
            PokedexItem(pokemon = pokemon)
        }
    }
}

/**
 * Representa la tarjeta individual de cada Pokémon (PokedexItem).
 * Utiliza [AsyncImage] para descargar y mostrar la imagen del Pokémon desde la web.
 *
 * @param pokemon Datos del Pokémon a renderizar.
 * @param modifier Modificador de diseño.
 */
@Composable
fun PokedexItem(
    pokemon: DatosPokemon,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 4.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Carga asíncrona de la imagen remota con Coil 3 reemplazando http por https
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pokemon.img.replace("http://", "https://"))
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.placeholder),
                error = painterResource(R.drawable.placeholder),
                contentDescription = pokemon.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .weight(0.3f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(0.7f)
            ) {
                Text(
                    text = "#${pokemon.num} ${pokemon.name}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (!pokemon.type.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${stringResource(R.string.pokemon_tipo)}: ${pokemon.type.joinToString(", ")}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
