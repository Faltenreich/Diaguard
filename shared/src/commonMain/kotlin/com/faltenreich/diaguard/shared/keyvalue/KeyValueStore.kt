package com.faltenreich.diaguard.shared.keyvalue

import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

interface KeyValueStore {

    fun <T: Any> read(kClass: KClass<T>, key: String): Flow<T?>

    suspend fun <T : Any> write(kClass: KClass<T>, key: String, value: T?)
}

inline fun <reified T: Any> KeyValueStore.read(key: String): Flow<T?> = read(T::class, key)

suspend inline fun <reified T: Any> KeyValueStore.write(key: String, value: T?) = write(T::class, key, value)