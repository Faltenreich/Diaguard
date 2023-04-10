package com.faltenreich.diaguard.shared.networking

interface NetworkingClient {

    suspend fun request(url: String): String
}