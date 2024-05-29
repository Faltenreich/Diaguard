package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.tag.Tag

/**
 * Stubbed implementation, since there is no previous iOS version to import from
 */
actual class LegacyDatabase : LegacyDao {

    actual override fun getEntries(): List<Entry.Legacy> {
        return emptyList()
    }

    actual override fun getMeasurementValues(): List<MeasurementValue.Legacy> {
        return emptyList()
    }

    actual override fun getTags(): List<Tag.Legacy> {
        return emptyList()
    }
}