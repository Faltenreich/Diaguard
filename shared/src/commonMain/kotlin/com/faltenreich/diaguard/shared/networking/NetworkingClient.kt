package com.faltenreich.diaguard.shared.networking

fun interface NetworkingClient {

    suspend fun request(request: NetworkingRequest): String
}