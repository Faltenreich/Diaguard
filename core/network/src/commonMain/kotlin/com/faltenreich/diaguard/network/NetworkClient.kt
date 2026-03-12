package com.faltenreich.diaguard.network

fun interface NetworkClient {

    suspend fun request(request: NetworkRequest): String
}