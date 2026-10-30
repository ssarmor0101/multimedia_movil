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

// Instancia de DataStore utilizando el delegado preferencesDataStore con Context como receptor
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = CONFIGURACION_NAME
)

class ConfiguracionDataStore(private val context: Context) {

    companion object {
        val CHECK_BOX = booleanPreferencesKey("check_box")
        val RADIO = stringPreferencesKey("radio")
    }

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
                // On the first run of the app
                preferences[clave] ?: defaultValue
            }
    }

    val checkBox: Flow<Boolean> = recoverFlow(CHECK_BOX, false)
    val radio: Flow<String> = recoverFlow(RADIO, "default")

    suspend fun guardarCheckBox(valor: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[CHECK_BOX] = valor
        }
    }

    suspend fun guardarRadio(valor: String) {
        context.dataStore.edit { preferencias ->
            preferencias[RADIO] = valor
        }
    }
}
