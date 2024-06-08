package com.faltenreich.diaguard.shared.networking

import kotlin.test.Test
import kotlin.test.assertEquals

class NetworkingRequestTest {

    @Test
    fun `generates url with host and path and arguments`() {
        val request = NetworkingRequest(
            host = "HOST",
            path = "PATH SPACED",
            arguments = mapOf(
                "FIRST" to "1",
                "SECOND" to "2 2"
            ),
        )
        val url = request.url()
        assertEquals(
            "https://HOST/PATH%20SPACED?FIRST=1&SECOND=2+2",
            url,
        )
    }
}