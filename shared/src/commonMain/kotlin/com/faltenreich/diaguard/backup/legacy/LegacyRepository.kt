package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.backup.legacy.measurement.EntryLegacy
import com.faltenreich.diaguard.backup.legacy.measurement.MeasurementValueLegacy
import com.faltenreich.diaguard.backup.legacy.measurement.TagLegacy

// TODO: Merge dedicated data classes into sealed interfaces
expect class LegacyRepository constructor() {

    fun getEntries(): List<EntryLegacy>

    fun getMeasurementValues(): List<MeasurementValueLegacy>

    fun getTags(): List<TagLegacy>
}