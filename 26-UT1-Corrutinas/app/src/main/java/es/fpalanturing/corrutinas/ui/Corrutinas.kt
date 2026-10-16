package es.fpalanturing.corrutinas.ui

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.fpalanturing.corrutinas.R
import es.fpalanturing.corrutinas.ui.theme.CorrutinasTheme
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

/**
 * PANTALLA PRINCIPAL DE DEMOSTRACIÓN DIDÁCTICA DE CORRUTINAS EN KOTLIN
 * ====================================================================
 * Esta pantalla demuestra conceptos fundamentales de la programación asíncrona:
 *  1. Demostración práctica de Bloqueo de UI (Thread.sleep vs delay).
 *  2. Hilo UI (Main) vs Hilos de Fondo (IO / Default) con 'withContext' y visualización del hilo activo.
 *  3. Cancelación cooperativa de tareas mediante la referencia a 'Job' y 'job.cancel()'.
 *  4. Emisión de flujos asíncronos reactivos con Kotlin 'Flow'.
 *  5. Ejecución concurrente en paralelo mediante 'async' y 'awaitAll()'.
 */
@Composable
fun Corrutinas(modifier: Modifier = Modifier) {
    /*
     * 'rememberCoroutineScope()':
     *  - Crea un CoroutineScope (ámbito) ligado al ciclo de vida del componente Composable.
     *  - VENTAJA DIDÁCTICA: Si el usuario navega fuera de esta pantalla o el componente se destruye,
     *    Compose cancelará AUTOMÁTICAMENTE todas las corrutinas lanzadas desde este scope,
     *    evitando fugas de memoria (Memory Leaks) y procesamiento en segundo plano innecesario.
     */
    val coroutineScope = rememberCoroutineScope()

    // =========================================================================
    // ESTADOS DE LA SECCIÓN 1: Bloqueo de UI vs Suspensión de Corrutinas
    // =========================================================================
    var estadoMensajeBloqueo by remember { mutableStateOf("Indicador girando normalmente") }

    // =========================================================================
    // ESTADOS DE LA SECCIÓN 2: Hilo de Ejecución y Tarea Pesada
    // =========================================================================
    var pulsadoSeccion2 by remember { mutableStateOf(false) }
    var nombreHiloActual by remember { mutableStateOf("Main (UI Thread)") }
    val textoResId = if (pulsadoSeccion2) R.string.txt_posterior else R.string.txt_inicial

    // =========================================================================
    // ESTADOS DE LA SECCIÓN 3: Cancelación Activa mediante Job
    // =========================================================================
    var downloadProgress1 by remember { mutableFloatStateOf(0.0f) }
    var loading1 by remember { mutableStateOf(false) }
    var estadoDescarga1Texto by remember { mutableStateOf("En espera") }
    /*
     * 'Job':
     *  - Es la referencia al ciclo de vida de una corrutina lanzada con launch {}.
     *  - VENTAJA DIDÁCTICA: Al guardar el 'Job' en el estado, podemos consultar si la tarea
     *    sigue activa (job.isActive) y cancelarla explícitamente en cualquier momento con job.cancel().
     */
    var jobDescarga1 by remember { mutableStateOf<Job?>(null) }

    // =========================================================================
    // ESTADOS DE LA SECCIÓN 4: Reactividad con Kotlin Flow
    // =========================================================================
    var downloadProgress2 by remember { mutableFloatStateOf(0.0f) }
    var loading2 by remember { mutableStateOf(false) }

    // =========================================================================
    // ESTADOS DE LA SECCIÓN 5: Ejecución Paralela con async / awaitAll
    // =========================================================================
    var ejecutandoEnParalelo by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ---------------------------------------------------------------------
        // SECCIÓN 1: DEMOSTRACIÓN DIDÁCTICA DE BLOQUEO DE UI (Thread.sleep vs delay)
        // ---------------------------------------------------------------------
        SeccionDemostracionBloqueoUI(
            estadoMensaje = estadoMensajeBloqueo,
            onProbandoConCorrutinaClick = {
                coroutineScope.launch {
                    estadoMensajeBloqueo = "Ejecutando delay(2500ms)... ¡El indicador SIGUE girando!"
                    /*
                     * 'delay(2500L)': SUSPENSIÓN NO BLOQUEANTE
                     *  - Libera el hilo principal (UI) durante 2.5 segundos.
                     *  - Como el hilo queda libre, Android continúa renderizando la pantalla
                     *    a 60/120 FPS y el CircularProgressIndicator gira con total fluidez.
                     */
                    delay(2500L.milliseconds)
                    estadoMensajeBloqueo = "Indicador girando normalmente"
                }
            },
            onProbandoSinCorrutinaClick = {
                estadoMensajeBloqueo = "¡Hilo Principal BLOQUEADO con Thread.sleep(2500ms)!"

                /*
                 * EXPLICACIÓN DIDÁCTICA CLAVE: ¿POR QUÉ SE CONGELA EL INDICADOR?
                 * =============================================================
                 * Al llamar a 'Thread.sleep(2500L)' directamente en el Hilo Principal (Main UI Thread):
                 *  1. Detenemos por completo el Hilo de la Interfaz de Usuario durante 2.5 segundos.
                 *  2. Android NO PUEDE renderizar nuevos fotogramas mientras el Hilo Principal está dormido.
                 *  3. RESULTADO VISUAL: El indicador giratorio SE DETIENE EN SECO a mitad del aire
                 *     y la aplicación no responde a ningún toque durante 2.5 segundos.
                 *  4. CURIOSIDAD: el mensaje "BLOQUEADO" de arriba nunca llega a verse, porque cambiar
                 *     el estado solo PROGRAMA un nuevo dibujado y el hilo dormido no puede realizarlo.
                 *     Cuando despierta, el estado ya ha vuelto a su valor anterior.
                 */
                Thread.sleep(2500L) // ¡BLOQUEA EL HILO PRINCIPAL Y CONGELA LA PANTALLA!

                estadoMensajeBloqueo = "Indicador girando normalmente"
            }
        )

        HorizontalDivider()

        // ---------------------------------------------------------------------
        // SECCIÓN 2: HILO UI vs HILOS SECUNDARIOS (withContext + Dispatchers)
        // ---------------------------------------------------------------------
        SeccionBotonMensaje(
            textoResId = textoResId,
            pulsado = pulsadoSeccion2,
            nombreHilo = nombreHiloActual,
            onButtonClick = {
                coroutineScope.launch {
                    pulsadoSeccion2 = true
                    nombreHiloActual = Thread.currentThread().name

                    /*
                     * 'withContext(Dispatchers.IO)':
                     *  - Cambia el hilo de ejecución de la corrutina a un hilo de fondo (pool de IO),
                     *    pensado para trabajo con mucha espera: red, ficheros, base de datos.
                     *  - Mientras dura el bloque, el hilo principal queda libre y la UI sigue fluida.
                     *    Si este trabajo se hiciera directamente en Main, la pantalla se congelaría
                     *    y podría aparecer un error ANR (Application Not Responding).
                     *  - Aquí el trabajo se simula con un bucle de 100 vueltas con delay(20 ms) (≈ 2 s),
                     *    lo bastante largo como para ver el cambio de hilo y el indicador en pantalla.
                     *  - Al terminar el bloque, la corrutina vuelve sola al dispatcher original (Main).
                     */
                    withContext(Dispatchers.IO) {
                        nombreHiloActual = Thread.currentThread().name // Muestra el hilo secundario (worker)
                        for (i in 1..100) {
                            delay(20L.milliseconds) // Simula 2 segundos de trabajo en segundo plano
                            if (i % 20 == 0) {
                                Log.i("CORRUTINAS_DEMO", "Progreso: $i% en hilo: ${Thread.currentThread().name}")
                            }
                        }
                    }

                    // Al salir de withContext, la corrutina vuelve automáticamente al Dispatcher original (Main)
                    nombreHiloActual = Thread.currentThread().name
                    pulsadoSeccion2 = false
                }
            }
        )

        HorizontalDivider()

        // ---------------------------------------------------------------------
        // SECCIÓN 3: CANCELACIÓN COOPERATIVA DE CORRUTINAS (Job.cancel())
        // ---------------------------------------------------------------------
        SeccionCancelacionJob(
            progress = downloadProgress1,
            loading = loading1,
            estadoTexto = estadoDescarga1Texto,
            onStartClick = {
                // Iniciar la corrutina y guardar la referencia al Job
                jobDescarga1 = coroutineScope.launch {
                    loading1 = true
                    estadoDescarga1Texto = "Descargando..."
                    try {
                        /*
                         * Cancelación cooperativa en Kotlin:
                         * 'delay()' suspende la corrutina y verifica si el Job ha sido cancelado.
                         * Si job.cancel() es invocado, delay() lanza inmediatamente una 'CancellationException'.
                         */
                        for (step in 1..100) {
                            delay(50L.milliseconds)
                            downloadProgress1 = step / 100f
                        }
                        estadoDescarga1Texto = "¡Completado con éxito!"
                    } catch (_: CancellationException) {
                        /*
                         * Atrapar la CancellationException nos permite reaccionar a la cancelación:
                         * actualizar la UI, limpiar recursos, registrar un log...
                         * (En código real suele volver a lanzarse con 'throw' tras limpiar; aquí
                         * no hace falta porque la corrutina termina en este punto.)
                         */
                        estadoDescarga1Texto = "Descarga cancelada por el usuario"
                        Log.w("CORRUTINAS_DEMO", "La descarga 1 fue cancelada.")
                    } finally {
                        // Se ejecuta SIEMPRE (éxito, error o cancelación): reactiva el botón Iniciar
                        loading1 = false
                    }
                }
            },
            onCancelClick = {
                /*
                 * 'job.cancel()':
                 * Cancela la corrutina activa inmediatamente en su siguiente punto de suspensión (delay/yield).
                 */
                jobDescarga1?.cancel()
            }
        )

        HorizontalDivider()

        // ---------------------------------------------------------------------
        // SECCIÓN 4: EMISIÓN REACTIVA DE DATOS CON KOTLIN FLOW
        // ---------------------------------------------------------------------
        SeccionFlow(
            progress = downloadProgress2,
            loading = loading2,
            onStartClick = {
                coroutineScope.launch {
                    loading2 = true
                    /*
                     * 'Flow.collect':
                     *  - VENTAJA DIDÁCTICA: Kotlin Flow es un flujo asíncrono reactivo frío (Cold Stream).
                     *  - 'flow {}' emite valores secuencialmente y 'collect {}' los consume en tiempo real.
                     *  - Es la arquitectura estándar recomendada en Android para desacoplar datos de la UI.
                     */
                    simularDescargaFlow().collect { progresoEmitido ->
                        downloadProgress2 = progresoEmitido
                    }
                    loading2 = false
                    delay(1000L.milliseconds)
                    downloadProgress2 = 0.0f
                }
            }
        )

        HorizontalDivider()

        // ---------------------------------------------------------------------
        // SECCIÓN 5: EJECUCIÓN PARALELA CON async / awaitAll
        // ---------------------------------------------------------------------
        SeccionEjecucionParalela(
            ejecutando = ejecutandoEnParalelo,
            onStartParaleloClick = {
                coroutineScope.launch {
                    ejecutandoEnParalelo = true
                    downloadProgress1 = 0.0f
                    downloadProgress2 = 0.0f

                    /*
                     * 'async { ... }':
                     *  - Lanza varias corrutinas a la vez (de forma CONCURRENTE): ninguna espera a la otra.
                     *  - 'async' devuelve un 'Deferred<T>' (un resultado "prometido").
                     *  - 'awaitAll()' suspende hasta que TODAS las tareas terminen.
                     *  - Tiempo total ≈ el de la tarea más lenta (5 s), en vez de la suma (3 s + 5 s = 8 s).
                     *  - OJO: ambas corren en Main y se turnan en ese hilo (casi no lo ocupan porque usan delay).
                     *    Para paralelismo real con cálculo intensivo se usaría Dispatchers.Default.
                     */
                    val tarea1 = async {
                        for (step in 1..100) {
                            delay(30L.milliseconds)
                            downloadProgress1 = step / 100f
                        }
                    }

                    val tarea2 = async {
                        for (step in 1..100) {
                            delay(50L.milliseconds)
                            downloadProgress2 = step / 100f
                        }
                    }

                    // Esperamos a que ambas tareas finalicen simultáneamente
                    awaitAll(tarea1, tarea2)
                    ejecutandoEnParalelo = false
                }
            }
        )
    }
}

/**
 * CREADOR DE KOTLIN FLOW (FLUJO REACTIVO)
 *  - 'flow {}' es el PRODUCTOR: emite valores con 'emit()' de forma secuencial.
 *  - 'flowOn(Dispatchers.Default)' cambia el hilo SOLO del productor (lo que hay antes de flowOn).
 *  - El 'collect' (consumidor) se sigue ejecutando en el hilo de la corrutina que lo llama (Main),
 *    por eso es seguro actualizar la UI dentro de él.
 */
private fun simularDescargaFlow(): Flow<Float> = flow {
    for (step in 1..100) {
        delay(40L.milliseconds)
        emit(step / 100f) // Emite cada porcentaje de avance
    }
}.flowOn(Dispatchers.Default)


// =============================================================================
// SUBCOMPONIBLES DE INTERFAZ DE USUARIO (UI)
// =============================================================================

@Composable
private fun SeccionDemostracionBloqueoUI(
    estadoMensaje: String,
    onProbandoConCorrutinaClick: () -> Unit,
    onProbandoSinCorrutinaClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "1. Comparativa: Suspensión vs Bloqueo de UI",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Text(
            text = estadoMensaje,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = Color(0xFF1976D2),
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        // Este indicador gira continuamente en la pantalla.
        // Sirve como testigo visual: si el hilo se bloquea (Thread.sleep), dejará de girar al instante.
        CircularProgressIndicator()

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onProbandoConCorrutinaClick
            ) {
                Text(text = "Con Corrutina (delay)")
            }

            OutlinedButton(
                onClick = onProbandoSinCorrutinaClick,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F))
            ) {
                Text(text = "Sin Corrutina (Thread.sleep)")
            }
        }
    }
}

@Composable
private fun SeccionBotonMensaje(
    textoResId: Int,
    pulsado: Boolean,
    nombreHilo: String,
    onButtonClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "2. Demostración de Cambio de Hilos (Dispatchers)",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Text(
            text = stringResource(id = textoResId),
            modifier = Modifier.background(if (pulsado) Color.Red else Color.Transparent),
            fontSize = 18.sp,
            softWrap = true,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Hilo de ejecución actual:\n[$nombreHilo]",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = if (pulsado) Color(0xFFD32F2F) else Color(0xFF388E3C),
            textAlign = TextAlign.Center
        )

        Button(
            onClick = onButtonClick,
            enabled = !pulsado
        ) {
            Text(text = stringResource(id = R.string.txt_botón))
        }

        if (pulsado) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun SeccionCancelacionJob(
    progress: Float,
    loading: Boolean,
    estadoTexto: String,
    onStartClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = stringResource(R.string.etiqueta_progreso)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "3. Cancelación Activa de Corrutinas (Job.cancel())",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Estado: $estadoTexto",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onStartClick,
                enabled = !loading
            ) {
                Text(text = stringResource(R.string.txt_botón))
            }

            OutlinedButton(
                onClick = onCancelClick,
                enabled = loading,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
            ) {
                Text(text = "Cancelar Descarga")
            }
        }
    }
}

@Composable
private fun SeccionFlow(
    progress: Float,
    loading: Boolean,
    onStartClick: () -> Unit
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = stringResource(R.string.etiqueta_progreso)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "4. Transmisión de Datos con Kotlin Flow",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Button(
            onClick = onStartClick,
            enabled = !loading
        ) {
            Text(text = "Iniciar Flow")
        }
    }
}

@Composable
private fun SeccionEjecucionParalela(
    ejecutando: Boolean,
    onStartParaleloClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "5. Concurrencia en Paralelo (async / awaitAll)",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Button(
            onClick = onStartParaleloClick,
            enabled = !ejecutando
        ) {
            Text(text = if (ejecutando) "Ejecutando en Paralelo..." else "Descargar 1 y 2 en Paralelo")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CorrutinasPreview() {
    CorrutinasTheme {
        Corrutinas()
    }
}