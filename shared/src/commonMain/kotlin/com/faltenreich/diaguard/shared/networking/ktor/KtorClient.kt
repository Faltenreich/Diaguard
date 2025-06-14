package com.faltenreich.diaguard.shared.networking.ktor

import com.faltenreich.diaguard.shared.networking.NetworkingClient
import com.faltenreich.diaguard.shared.networking.NetworkingRequest
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.request
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlin.time.Duration.Companion.seconds

class KtorClient(
    private val client: HttpClient = HttpClient {
        install(ContentNegotiation) { json() }
        install(HttpTimeout) { requestTimeoutMillis = TIMEOUT_IN_SECONDS.seconds.inWholeMilliseconds }
    },
): NetworkingClient {

    override suspend fun request(request: NetworkingRequest): String {
        val response = client.request(request.url())
        return response.bodyAsText()
    }

    companion object {

        private const val TIMEOUT_IN_SECONDS = 10
    }
}