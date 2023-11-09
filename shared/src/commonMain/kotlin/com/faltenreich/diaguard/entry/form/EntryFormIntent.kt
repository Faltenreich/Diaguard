package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.form.measurement.MeasurementTypeInputData

sealed interface EntryFormIntent {

    data class Edit(val data: MeasurementTypeInputData) : EntryFormIntent

    data object Submit : EntryFormIntent

    data object Delete : EntryFormIntent

    data object AddFood : EntryFormIntent
}