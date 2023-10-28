package com.faltenreich.diaguard.shared.serialization

import app.softwork.serialization.csv.CSVFormat
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.mamoe.yamlkt.Yaml

class KotlinxSerialization {

    @PublishedApi internal val csv = CSVFormat(
        separator = ";",
    )

    @PublishedApi internal val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @PublishedApi internal val yaml = Yaml

    inline fun <reified T: Any> encodeCsv(data: T): String {
        return this.csv.encodeToString(data)
    }

    inline fun <reified T : Any> decodeCsv(csv: String): T {
        return this.csv.decodeFromString(csv)
    }

    inline fun <reified T: Any> encodeJson(data: T): String {
        return this.json.encodeToString(data)
    }

    inline fun <reified T : Any> decodeJson(json: String): T {
        return this.json.decodeFromString(json)
    }

    inline fun <reified T : Any> encodeYaml(data: T): String {
        return this.yaml.encodeToString(data)
    }

    inline fun <reified T : Any> decodeYaml(yaml: String): T {
        return this.yaml.decodeFromString(yaml)
    }
}