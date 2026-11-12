package es.alan_turing.linkfp.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val CONFIGURACION_NAME = "configuracion"

// Instancia única de DataStore delegada mediante preferencesDataStore asociándola al Context
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = CONFIGURACION_NAME
)

/**
 * Gestor del almacenamiento persistente de preferencias de la aplicación mediante Preferences DataStore.
 *
 * @property context El contexto necesario de la aplicación para interactuar con la fuente de datos de DataStore.
 */
class ConfiguracionDataStore(private val context: Context) {

    companion object {
        /** Clave para guardar y recuperar el valor booleano del CheckBox. */
        val CHECK_BOX = booleanPreferencesKey("check_box")
        /** Clave para guardar y recuperar el valor tipo cadena de la opción Radio. */
        val RADIO = stringPreferencesKey("radio")
        /** Clave para guardar y recuperar la opción seleccionada en el Dropdown. */
        val DROPDOWN = stringPreferencesKey("dropdown")
    }

    /**
     * Recupera un flujo ([Flow]) de datos asociado a una clave específica de Preferences.
     * En caso de producirse un error de E/S ([IOException]), captura la excepción y emite preferencias vacías.
     *
     * @param T Tipo de dato que almacena la preferencia.
     * @param clave La clave de la preferencia a recuperar.
     * @param defaultValue Valor por defecto que se retornará en caso de que la clave no esté definida.
     * @return [Flow] que emite de forma reactiva el valor de la preferencia solicitada.
     */
    fun <T> recoverFlow(clave: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return context.dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }
            .map { preferences ->
                // Retorna la preferencia guardada o el valor por defecto en caso de no existir
                preferences[clave] ?: defaultValue
            }
    }

    /** Flow reactivo que emite el estado guardado del CheckBox (por defecto: false). */
    val checkBox: Flow<Boolean> = recoverFlow(CHECK_BOX, false)

    /** Flow reactivo que emite el estado guardado del grupo Radio (por defecto: "default"). */
    val radio: Flow<String> = recoverFlow(RADIO, "default")

    /** Flow reactivo que emite el estado guardado del Dropdown (por defecto: "Opción A"). */
    val dropdown: Flow<String> = recoverFlow(DROPDOWN, "Opción A")

    /**
     * Persiste el estado del CheckBox en el almacén de DataStore.
     *
     * @param valor Nuevo estado booleano.
     */
    suspend fun guardarCheckBox(valor: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[CHECK_BOX] = valor
        }
    }

    /**
     * Persiste la opción seleccionada del grupo Radio en el almacén de DataStore.
     *
     * @param valor Nuevo valor de texto asignado a la opción radio.
     */
    suspend fun guardarRadio(valor: String) {
        context.dataStore.edit { preferencias ->
            preferencias[RADIO] = valor
        }
    }

    /**
     * Persiste la opción seleccionada del menú desplegable (Dropdown) en el almacén de DataStore.
     *
     * @param valor Nuevo valor de texto seleccionado del menú desplegable.
     */
    suspend fun guardarDropdown(valor: String) {
        context.dataStore.edit { preferencias ->
            preferencias[DROPDOWN] = valor
        }
    }
}

