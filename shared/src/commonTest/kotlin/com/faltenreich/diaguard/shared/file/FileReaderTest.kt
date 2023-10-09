package com.faltenreich.diaguard.shared.file

import kotlin.test.Test
import kotlin.test.assertNotNull

class FileReaderTest {

    private val fileReader = FileReader()

    @Test
    fun `reads file`() {
        assertNotNull(fileReader.readFile("src/commonTest/resources/text.txt"))
    }
}