package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.backup.legacy.measurement.MeasurementValueLegacy
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.tag.Tag

expect class LegacyRepository constructor() {

    fun getEntries(): List<Entry.Legacy>

    fun getMeasurementValues(): List<MeasurementValueLegacy>

    fun getTags(): List<Tag.Legacy>
}