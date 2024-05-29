package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.tag.Tag

expect class LegacyDatabase constructor() : LegacyDao {

    override fun getEntries(): List<Entry.Legacy>

    override fun getMeasurementValues(): List<MeasurementValue.Legacy>

    override fun getTags(): List<Tag.Legacy>
}