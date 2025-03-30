package com.faltenreich.diaguard.shared.networking.ktor

import com.faltenreich.diaguard.shared.networking.NetworkingRequest
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class KtorClientTest {

    @Test
    fun `responds with expected json`() = runBlocking {
        val json = """{"key":"value"}"""

        val httpClientEngine = MockEngine { respond(content = ByteReadChannel(json)) }
        val httpClient = HttpClient(httpClientEngine)

        val client = KtorClient(httpClient)
        val request = NetworkingRequest(host = "", path = "")
        val response = client.request(request)

        assertEquals(json, response)
    }
}