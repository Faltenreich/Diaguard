package com.faltenreich.rhyme.shared.networking

interface NetworkingClient {

    suspend fun request(url: String): String
}