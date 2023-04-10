package com.faltenreich.diaguard.shared.serialization

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class KotlinxSerialization {

    @PublishedApi internal val parser = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    inline fun <reified T: Any> encode(data: T): String {
        return parser.encodeToString(data)
    }

    inline fun <reified T : Any> decode(json: String): T {
        return parser.decodeFromString(json)
    }
}