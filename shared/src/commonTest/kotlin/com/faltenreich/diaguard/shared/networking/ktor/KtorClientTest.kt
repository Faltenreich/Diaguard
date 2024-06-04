package com.faltenreich.diaguard.shared.networking.ktor

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class KtorClientTest {

    @Test
    fun `responds with expected json`() = runBlocking {
        val json = """{"key":"value"}"""
        val mockEngine = MockEngine {
            respond(content = ByteReadChannel(json))
        }
        val httpClient = HttpClient(mockEngine)
        val client = KtorClient(httpClient)
        assertEquals(json, client.request(""))
    }
}