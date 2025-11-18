package com.faltenreich.diaguard.serialization

// This proxy is a workaround since interfaces cannot have inline functions
// which are required for kotlinx.serialization to work properly with generics
class Serialization {

    @PublishedApi internal val implementation = KotlinxSerialization()

    inline fun <reified T: Any> encodeCsv(data: T): String {
        return implementation.encodeCsv(data)
    }

    inline fun <reified T: Any> decodeCsv(csv: String): List<T> {
        return implementation.decodeCsv(csv)
    }

    inline fun <reified T: Any> encodeJson(data: T): String {
        return implementation.encodeJson(data)
    }

    inline fun <reified T: Any> decodeJson(json: String): T {
        return implementation.decodeJson(json)
    }

    inline fun <reified T: Any> encodeYaml(data: T): String {
        return implementation.encodeYaml(data)
    }

    inline fun <reified T: Any> decodeYaml(yaml: String): T {
        return implementation.decodeYaml(yaml)
    }
}