package com.faltenreich.diaguard.shared.serialization

// This proxy is a workaround since interfaces cannot have inline functions
// which are required for kotlinx.serialization to work properly with generics
class JsonSerialization {

    @PublishedApi internal val implementation = KotlinxSerialization()

    inline fun <reified T: Any> encode(data: T): String {
        return implementation.encode(data)
    }

    inline fun <reified T: Any> decode(json: String): T {
        return implementation.decode(json)
    }
}