package com.faltenreich.diaguard.shared.serialization

import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals

class KotlinxSerializationTest {

    private val serialization = KotlinxSerialization()
    private val csvData = CsvDto(firstName = "John", lastName = "Doe")
    private val data = Dto(one = "a", many = listOf("a", "b"))
    private val csv = """
       firstName;lastName
       John;Doe
    """.trimIndent()
    private val json = """
        {
            "one": "a",
            "many": [
                "a",
                "b"
            ]
        }
    """
    private val yaml = """
        one: a
        many:
            - a
            - b
    """

    @Serializable
    private data class CsvDto(val firstName: String, val lastName: String)

    @Serializable
    private data class Dto(val one: String, val many: List<String>)

    @Test
    fun `encodes to CSV`() {
        assertEquals(
            expected = csv,
            actual = serialization.encodeCsv(csvData),
        )
    }

    @Test
    fun `decodes from CSV`() {
        assertEquals(
            expected = csvData,
            actual = serialization.decodeCsv(csv),
        )
    }

    @Test
    fun `encodes to JSON`() {
        assertEquals(
            expected = json.filterNot(Char::isWhitespace),
            actual = serialization.encodeJson(data),
        )
    }

    @Test
    fun `decodes from JSON`() {
        assertEquals(
            expected = data,
            actual = serialization.decodeJson(json),
        )
    }

    @Test
    fun `encodes to YAML`() {
        assertEquals(
            expected = "\"" + yaml.replace("\n","\\n") + "\"",
            actual = serialization.encodeYaml(yaml),
        )
    }

    @Test
    fun `decodes from YAML`() {
        assertEquals(
            expected = data,
            actual = serialization.decodeYaml(yaml),
        )
    }
}