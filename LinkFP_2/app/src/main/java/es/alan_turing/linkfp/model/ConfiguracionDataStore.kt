package es.alan_turing.linkfp.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val CONFIGURACION_NAME = "configuracion"

// Create a DataStore instance using the preferencesDataStore delegate, with the Context as
// receiver.
private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(
    name = CONFIGURACION_NAME
)

class ConfiguracionDataStore(context: Context) {
    private val CLAVE = stringPreferencesKey("clave")
    private val VALOR = stringPreferencesKey("valor")

    val preferenceFlow: Flow<String> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            // On the first run of the app, we will use LinearLayoutManager by default
            preferences[CLAVE] ?: "default"
        }

    suspend fun saveClaveToPreferencesStore(clave: String, context: Context) {
        context.dataStore.edit { preferences ->
            preferences[CLAVE] = clave
        }
    }
}