package com.faltenreich.diaguard.shared.keyvalue

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

actual class KeyValueStore(
    private val context: Context,
    name: String = STORE_NAME_DEFAULT,
) {

    private val Context.dataStore by preferencesDataStore(name = name)

    @PublishedApi internal fun <T> read(transform: (Preferences) -> T): Flow<T> {
        return context.dataStore.data.map(transform)
    }

    @PublishedApi internal suspend fun write(transform: (MutablePreferences) -> Unit) {
        context.dataStore.edit(transform)
    }

    @Suppress("UNCHECKED_CAST")
    @PublishedApi internal inline fun <reified T> getKey(key: String): Preferences.Key<T> {
        return when (T::class) {
            Int::class -> intPreferencesKey(key)
            Double::class -> doublePreferencesKey(key)
            String::class -> stringPreferencesKey(key)
            Boolean::class -> booleanPreferencesKey(key)
            Float::class -> floatPreferencesKey(key)
            Long::class -> longPreferencesKey(key)
            Set::class -> stringSetPreferencesKey(key)
            else -> throw IllegalArgumentException("Unsupported class: ${T::class}")
        } as Preferences.Key<T>
    }

    actual inline fun <reified T> read(key: String): Flow<T?> {
        val preferencesKey = getKey<T>(key)
        return read { preferences -> preferences[preferencesKey] }
    }

    actual suspend inline fun <reified T> write(key: String, value: T) {
        val preferencesKey = getKey<T>(key)
        write { preferences -> preferences[preferencesKey] = value }
    }

    companion object {

        private const val STORE_NAME_DEFAULT = "user_preferences"
    }
}