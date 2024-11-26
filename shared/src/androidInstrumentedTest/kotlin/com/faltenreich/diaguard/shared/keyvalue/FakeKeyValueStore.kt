package com.faltenreich.diaguard.shared.keyvalue

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.reflect.KClass

class FakeKeyValueStore<T>(private val mock: Pair<String, T>) : KeyValueStore {

    override fun <T : Any> read(kClass: KClass<T>, key: String): Flow<T?> {
        val value = when (key) {
            mock.first -> mock.second
            else -> null
        }
        @Suppress("UNCHECKED_CAST")
        return flowOf(value as? T)
    }

    override suspend fun <T : Any> write(
        kClass: KClass<T>,
        key: String,
        value: T
    ) = Unit
}