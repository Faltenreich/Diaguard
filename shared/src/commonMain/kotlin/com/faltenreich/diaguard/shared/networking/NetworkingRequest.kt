package com.faltenreich.diaguard.shared.networking

import com.faltenreich.diaguard.shared.primitive.format

data class NetworkingRequest(
    private val host: String,
    private val path: String,
    private val arguments: Map<String, String>? = null,
) {

    fun url(): String {
        return "%s/%s?%s".format(
            host,
            path,
            arguments
                ?.map { (key, value) -> "$key=$value" }
                ?.joinToString("&"),
        )
    }

    override fun toString(): String {
        return url()
    }
}