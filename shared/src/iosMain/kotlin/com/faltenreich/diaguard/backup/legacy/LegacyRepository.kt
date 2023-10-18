package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.backup.legacy.measurement.EntryLegacy
import com.faltenreich.diaguard.backup.legacy.measurement.MeasurementValueLegacy
import com.faltenreich.diaguard.backup.legacy.measurement.TagLegacy

/**
 * Stubbed implementation, since there is no previous iOS version to import from
 */
actual class LegacyRepository {

    actual fun getEntries(): List<EntryLegacy> = emptyList()

    actual fun getMeasurementValues(): List<MeasurementValueLegacy> = emptyList()

    actual fun getTags(): List<TagLegacy> = emptyList()
}