package com.faltenreich.diaguard.shared.keyvalue

import android.content.Context
import androidx.preference.PreferenceManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.reflect.KClass

class SharedPreferences(context: Context) : KeyValueStore {

    private val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    override fun <T : Any> read(kClass: KClass<T>, key: String): Flow<T?> {
        val value = when (kClass::class) {
            Boolean::class -> defaultSharedPreferences.getBoolean(key, false)
            String::class -> defaultSharedPreferences.getString(key, null)
            Int::class -> defaultSharedPreferences.getInt(key, -1)
            Long::class -> defaultSharedPreferences.getLong(key, -1L)
            Float::class -> defaultSharedPreferences.getFloat(key, -1f)
            else -> IllegalArgumentException("Unsupported type: ${kClass.simpleName}")
        }
        @Suppress("UNCHECKED_CAST")
        return flowOf(value as? T)
    }

    override suspend fun <T : Any> write(kClass: KClass<T>, key: String, value: T) {
        with(defaultSharedPreferences.edit()) {
            when (kClass::class) {
                Boolean::class -> putBoolean(key, value as Boolean)
                String::class -> putString(key, value as String)
                Int::class -> putInt(key, value as Int)
                Long::class -> putLong(key, value as Long)
                Float::class -> putFloat(key, value as Float)
                else -> IllegalArgumentException("Unsupported type: ${kClass.simpleName}")
            }
            apply()
        }
    }
}