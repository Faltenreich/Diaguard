package com.faltenreich.diaguard.shared.networking

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.request
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json

class KtorClient(
    private val client: HttpClient = HttpClient { install(ContentNegotiation) { json() } },
): NetworkingClient {

    override suspend fun request(url: String): String {
        val response = client.request(url)
        return response.bodyAsText()
    }
}