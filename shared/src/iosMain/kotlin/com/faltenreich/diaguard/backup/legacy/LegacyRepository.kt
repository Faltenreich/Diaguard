package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.backup.legacy.measurement.MeasurementValueLegacy
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.tag.Tag

/**
 * Stubbed implementation, since there is no previous iOS version to import from
 */
actual class LegacyRepository {

    actual fun getEntries(): List<Entry.Legacy> = emptyList()

    actual fun getMeasurementValues(): List<MeasurementValueLegacy> = emptyList()

    actual fun getTags(): List<Tag.Legacy> = emptyList()
}