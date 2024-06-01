package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.tag.Tag

class LegacyFakeDao : LegacyDao {

    override fun getEntries(): List<Entry.Legacy> {
        return emptyList()
    }

    override fun getMeasurementValues(): List<MeasurementValue.Legacy> {
        return emptyList()
    }

    override fun getTags(): List<Tag.Legacy> {
        return emptyList()
    }
}