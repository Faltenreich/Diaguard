package com.faltenreich.diaguard.network

import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.parameters

data class NetworkRequest(
    private val host: String,
    private val path: String,
    private val arguments: Map<String, String>? = null,
) {

    fun url(): String {
        return URLBuilder(
            protocol = URLProtocol.HTTPS,
            host = host,
            pathSegments = listOf(path),
            parameters = parameters {
                arguments?.forEach { (key, value) ->
                    append(key, value)
                }
            }
        ).buildString()
    }

    override fun toString(): String {
        return url()
    }
}