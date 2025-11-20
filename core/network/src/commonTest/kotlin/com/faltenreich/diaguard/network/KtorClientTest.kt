package com.faltenreich.diaguard.network

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

        val httpClientEngine = MockEngine.Companion { respond(content = ByteReadChannel(json)) }
        val httpClient = HttpClient(httpClientEngine)

        val client = KtorClient(httpClient)
        val request = NetworkRequest(host = "", path = "")
        val response = client.request(request)

        assertEquals(json, response)
    }
}