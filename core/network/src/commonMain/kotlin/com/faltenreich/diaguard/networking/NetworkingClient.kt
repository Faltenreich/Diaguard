package com.faltenreich.diaguard.networking

fun interface NetworkingClient {

    suspend fun request(request: NetworkingRequest): String
}