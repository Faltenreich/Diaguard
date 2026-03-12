package com.faltenreich.diaguard.persistence.keyvalue

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.reflect.KClass

class FakeKeyValueStore<T>(private vararg val mocks: Pair<String, T>) : KeyValueStore {

    override suspend fun exists(): Boolean {
        return true
    }

    override fun <T : Any> read(kClass: KClass<T>, key: String): Flow<T?> {
        val value = mocks.firstOrNull { it.first == key }?.second
        @Suppress("UNCHECKED_CAST")
        return flowOf(value as? T)
    }

    override suspend fun <T : Any> write(
        kClass: KClass<T>,
        key: String,
        value: T
    ) = Unit
}