package com.faltenreich.diaguard.shared.networking

import kotlin.test.Test
import kotlin.test.assertEquals

class NetworkingRequestTest {

    @Test
    fun `generates url with host and path and arguments`() {
        val request = NetworkingRequest(
            host = "HOST",
            path = "PATH",
            arguments = mapOf(
                "STRING" to "VALUE",
                "INTEGER" to 0,
            )
        )
        val url = request.url()
        assertEquals(
            "HOST/PATH?STRING=VALUE&INTEGER=0",
            url,
        )
    }
}