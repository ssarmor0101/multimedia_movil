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

// Instancia de DataStore utilizando el delegado preferencesDataStore con Context como receptor
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = CONFIGURACION_NAME
)

data class ConfiguracionData(
    val clave: String = "default",
    val valor: String = "default"
)

class ConfiguracionDataStore(private val context: Context) {

    companion object {
        val CLAVE = stringPreferencesKey("clave")
        val VALOR = stringPreferencesKey("valor")
    }

    // Flow con ambos valores establecidos (clave y valor)
    val preferenceFlow: Flow<ConfiguracionData> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                exception.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val clave = preferences[CLAVE] ?: "default"
            val valor = preferences[VALOR] ?: "default"
            ConfiguracionData(clave = clave, valor = valor)
        }

    // Flows individuales opcionales para acceso directo
    val claveFlow: Flow<String> = preferenceFlow.map { it.clave }
    val valorFlow: Flow<String> = preferenceFlow.map { it.valor }

    // Guardar clave
    suspend fun saveClaveToPreferencesStore(clave: String, context: Context = this.context) {
        context.dataStore.edit { preferences ->
            preferences[CLAVE] = clave
        }
    }

    // Guardar valor
    suspend fun saveValorToPreferencesStore(valor: String, context: Context = this.context) {
        context.dataStore.edit { preferences ->
            preferences[VALOR] = valor
        }
    }

    // Guardar clave y valor simultáneamente
    suspend fun saveConfiguracionToPreferencesStore(
        clave: String,
        valor: String,
        context: Context = this.context
    ) {
        context.dataStore.edit { preferences ->
            preferences[CLAVE] = clave
            preferences[VALOR] = valor
        }
    }

    // Métodos alias simplificados
    suspend fun saveClave(clave: String) = saveClaveToPreferencesStore(clave)
    suspend fun saveValor(valor: String) = saveValorToPreferencesStore(valor)
    suspend fun saveConfiguracion(clave: String, valor: String) =
        saveConfiguracionToPreferencesStore(clave, valor)
}
