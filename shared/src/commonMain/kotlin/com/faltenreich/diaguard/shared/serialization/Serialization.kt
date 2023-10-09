package com.faltenreich.diaguard.shared.serialization

// This proxy is a workaround since interfaces cannot have inline functions
// which are required for kotlinx.serialization to work properly with generics
class Serialization {

    @PublishedApi internal val implementation = KotlinxSerialization()

    inline fun <reified T: Any> encodeJson(data: T): String {
        return implementation.encodeJson(data)
    }

    inline fun <reified T: Any> decodeJson(json: String): T {
        return implementation.decodeJson(json)
    }

    inline fun <reified T: Any> encodeYaml(data: T): String {
        return implementation.encodeYaml(data)
    }

    inline fun <reified T: Any> decodeYaml(json: String): T {
        return implementation.decodeYaml(json)
    }
}