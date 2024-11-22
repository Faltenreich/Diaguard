package com.faltenreich.diaguard.shared.keyvalue

import android.content.Context
import androidx.preference.PreferenceManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.reflect.KClass

class SharedPreferences(context: Context) : KeyValueStore {

    private val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    override fun <T : Any> read(kClass: KClass<T>, key: String): Flow<T?> {
        val value = when (kClass) {
            Boolean::class -> defaultSharedPreferences.getBoolean(key, false)
            Int::class -> defaultSharedPreferences.getInt(key, -1).takeIf { it >= 0 }
            Long::class -> defaultSharedPreferences.getLong(key, -1L).takeIf { it >= 0L }
            Float::class -> defaultSharedPreferences.getFloat(key, -1f).takeIf { it >= 0f }
            String::class -> defaultSharedPreferences.getString(key, null)
            else -> IllegalArgumentException("Unsupported type: ${kClass.simpleName}")
        }
        @Suppress("UNCHECKED_CAST")
        return flowOf(value as? T)
    }

    override suspend fun <T : Any> write(kClass: KClass<T>, key: String, value: T) {
        with(defaultSharedPreferences.edit()) {
            when (kClass::class) {
                Boolean::class -> putBoolean(key, value as Boolean)
                Int::class -> putInt(key, value as Int)
                Long::class -> putLong(key, value as Long)
                Float::class -> putFloat(key, value as Float)
                String::class -> putString(key, value as String)
                else -> IllegalArgumentException("Unsupported type: ${kClass.simpleName}")
            }
            apply()
        }
    }
}