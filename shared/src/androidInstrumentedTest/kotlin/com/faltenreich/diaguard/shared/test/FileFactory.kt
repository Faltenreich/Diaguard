package com.faltenreich.diaguard.shared.test

import android.os.FileUtils
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import java.io.FileOutputStream

object FileFactory {

    fun createFromAssets(path: String): File {
        val context = InstrumentationRegistry.getInstrumentation().context
        val inputStream = context.resources.assets.open(path)
        val file = File(context.externalCacheDir, path)
        val outputStream = FileOutputStream(file)
        FileUtils.copy(inputStream, outputStream)
        return file
    }
}