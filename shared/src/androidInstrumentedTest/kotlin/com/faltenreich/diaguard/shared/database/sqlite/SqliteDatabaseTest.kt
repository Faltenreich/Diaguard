package com.faltenreich.diaguard.shared.database.sqlite

import android.os.FileUtils
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.FileOutputStream


@RunWith(AndroidJUnit4::class)
class SqliteDatabaseTest {

    @Test
    fun readsData() {
        val path = "diaguard.db"

        val context = InstrumentationRegistry.getInstrumentation().context
        val inputStream = context.resources.assets.open(path)

        val file = File(context.externalCacheDir, path)
        val outputStream = FileOutputStream(file)
        FileUtils.copy(inputStream, outputStream)

        val database = SqliteDatabase(file)

        val ids = mutableListOf<Long?>()
        database.query("entry") {
            ids.add(getLong("_id"))
        }

        Assert.assertTrue(ids.filterNotNull().isNotEmpty())
    }
}