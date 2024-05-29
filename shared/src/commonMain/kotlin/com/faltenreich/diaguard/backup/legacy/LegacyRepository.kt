package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.tag.Tag

class LegacyRepository(private val dao: LegacyDao) {

    fun getEntries(): List<Entry.Legacy> {
        return dao.getEntries()
    }

    fun getMeasurementValues(): List<MeasurementValue.Legacy> {
        return dao.getMeasurementValues()
    }

    fun getTags(): List<Tag.Legacy> {
        return dao.getTags()
    }
}