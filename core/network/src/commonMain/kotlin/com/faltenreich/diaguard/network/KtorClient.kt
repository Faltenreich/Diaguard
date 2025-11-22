package com.faltenreich.diaguard.network

import io.ktor.client.HttpClient
import io.ktor.client.request.request
import io.ktor.client.statement.bodyAsText

class KtorClient(
    private val client: HttpClient,
): NetworkClient {

    override suspend fun request(request: NetworkRequest): String {
        val response = client.request(request.url())
        return response.bodyAsText()
    }
}