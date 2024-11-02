package com.faltenreich.diaguard.export.pdf

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class AndroidPdfExportTest {

    @Test
    fun testExport() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val file = File.createTempFile("tmp", "pdf", context.cacheDir)
        val export = AndroidPdfExport(context)
        // TODO: Inject file
        export.export(
            document {  }
        )
    }
}