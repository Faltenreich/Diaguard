package com.faltenreich.diaguard.shared.networking

fun interface NetworkingClient {

    suspend fun request(url: String): String
}