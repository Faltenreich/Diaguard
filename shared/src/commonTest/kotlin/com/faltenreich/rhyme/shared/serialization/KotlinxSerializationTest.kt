package com.faltenreich.rhyme.shared.serialization

import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals

class KotlinxSerializationTest {

    private val serialization = KotlinxSerialization()
    private val data = Dto(property = "value")
    private val json = "{\"property\":\"value\"}"

    @Serializable
    private data class Dto(val property: String)

    @Test
    fun `encodes data to json`() {
        assertEquals(json, serialization.encode(data))
    }

    @Test
    fun `decodes json to data`() {
        assertEquals(data, serialization.decode(json))
    }
}