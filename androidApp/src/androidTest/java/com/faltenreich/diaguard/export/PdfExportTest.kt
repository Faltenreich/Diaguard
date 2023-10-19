package com.faltenreich.diaguard.export

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class PdfExportTest {

    @Test
    fun testExport() {
        val directory = InstrumentationRegistry.getInstrumentation().targetContext.cacheDir
        val file = File.createTempFile("tmp", "pdf", directory)
        PdfExport().export(file)
    }
}