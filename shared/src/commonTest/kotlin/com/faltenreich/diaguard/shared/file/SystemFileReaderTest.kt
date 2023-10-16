package com.faltenreich.diaguard.shared.file

import kotlin.test.Test
import kotlin.test.assertNotNull

class SystemFileReaderTest {

    private val systemFileReader = SystemFileReader("src/commonTest/resources/text.txt")

    @Test
    fun `reads file`() {
        assertNotNull(systemFileReader.read())
    }
}