package com.faltenreich.diaguard.shared.networking

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*

class KtorClient(
    private val client: HttpClient = HttpClient { install(ContentNegotiation) { json() } },
): NetworkingClient {

    override suspend fun request(url: String): String {
        val response = client.request(url)
        return response.bodyAsText()
    }
}