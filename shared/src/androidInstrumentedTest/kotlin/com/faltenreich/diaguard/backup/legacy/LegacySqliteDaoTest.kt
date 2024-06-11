package com.faltenreich.diaguard.backup.legacy

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LegacySqliteDaoTest {

    // FIXME: KoinApplication has not been started
    private val dao = LegacySqliteDao()

    @Test
    fun readsEntries() {
        Assert.assertTrue(dao.getEntries().isNotEmpty())
    }

    @Test
    fun readsMeasurements() {
        Assert.assertTrue(dao.getMeasurementValues().isNotEmpty())
    }

    @Test
    fun readsTags() {
        Assert.assertTrue(dao.getTags().isNotEmpty())
    }
}