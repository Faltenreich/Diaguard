package com.faltenreich.diaguard.shared.keyvalue

import kotlinx.coroutines.flow.Flow

expect class KeyValueStore {

    inline fun <reified T> read(key: String): Flow<T?>

    suspend inline fun <reified T> write(key: String, value: T)
}