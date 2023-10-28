package com.faltenreich.diaguard.shared.serialization

import app.softwork.serialization.csv.CSVFormat
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.mamoe.yamlkt.Yaml

class KotlinxSerialization {

    @PublishedApi internal val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    inline fun <reified T: Any> encodeCsv(data: T): String {
        return CSVFormat.encodeToString(data)
    }

    inline fun <reified T : Any> decodeCsv(csv: String): T {
        return CSVFormat.decodeFromString(csv)
    }

    inline fun <reified T: Any> encodeJson(data: T): String {
        return json.encodeToString(data)
    }

    inline fun <reified T : Any> decodeJson(json: String): T {
        return this.json.decodeFromString(json)
    }

    inline fun <reified T : Any> encodeYaml(data: T): String {
        return Yaml.encodeToString(data)
    }

    inline fun <reified T : Any> decodeYaml(yaml: String): T {
        return Yaml.decodeFromString(yaml)
    }
}