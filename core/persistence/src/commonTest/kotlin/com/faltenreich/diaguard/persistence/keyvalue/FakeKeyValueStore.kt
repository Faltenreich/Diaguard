package com.faltenreich.diaguard.persistence.keyvalue

import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.reflect.KClass

class FakeKeyValueStore : KeyValueStore {

    private val cache = SnapshotStateMap<String, Any>()

    override suspend fun exists(): Boolean {
        return cache.isNotEmpty()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> read(kClass: KClass<T>, key: String): Flow<T?> {
        return snapshotFlow { cache }.map { it[key] as? T }
    }

    override suspend fun <T : Any> write(kClass: KClass<T>, key: String, value: T) {
        cache[key] = value
    }
}