package com.faltenreich.diaguard.shared.keyvalue

import kotlinx.coroutines.flow.Flow

actual class KeyValueStore {

    actual inline fun <reified T> read(key: String): Flow<T?> {
        TODO("Not yet implemented")
    }

    actual suspend inline fun <reified T> write(key: String, value: T) {
        TODO("Not yet implemented")
    }
}