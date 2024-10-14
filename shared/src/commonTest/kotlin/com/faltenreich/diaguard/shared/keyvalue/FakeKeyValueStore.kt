package com.faltenreich.diaguard.shared.keyvalue

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.reflect.KClass

class FakeKeyValueStore : KeyValueStore {

    private val cache = mutableMapOf<String, Any>()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> read(kClass: KClass<T>, key: String): Flow<T?> {
        return flowOf(cache[key] as? T)
    }

    override suspend fun <T : Any> write(kClass: KClass<T>, key: String, value: T) {
        cache[key] = value
    }
}