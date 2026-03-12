package com.faltenreich.diaguard.persistence.file

import kotlin.test.Test
import kotlin.test.assertNotNull

class SystemFileReaderTest {

    private val systemFileReader = SystemFileReader("src/commonTest/resources/test.txt")

    @Test
    fun `reads file`() {
        assertNotNull(systemFileReader.read())
    }
}