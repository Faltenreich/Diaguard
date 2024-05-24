package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.tag.Tag

/**
 * Stubbed implementation, since there is no previous iOS version to import from
 */
actual class LegacyRepository {

    actual fun getEntries(): List<Entry.Legacy> = emptyList()

    actual fun getMeasurementValues(): List<MeasurementValue.Legacy> = emptyList()

    actual fun getTags(): List<Tag.Legacy> = emptyList()
}