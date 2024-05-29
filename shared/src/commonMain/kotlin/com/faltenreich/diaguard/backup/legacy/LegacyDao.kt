package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.tag.Tag

interface LegacyDao {

    fun getEntries(): List<Entry.Legacy>

    fun getMeasurementValues(): List<MeasurementValue.Legacy>

    fun getTags(): List<Tag.Legacy>
}