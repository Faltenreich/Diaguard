package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.form.measurement.MeasurementTypeInputData

sealed interface EntryFormIntent {

    data class ChangeTypeInput(val data: MeasurementTypeInputData) : EntryFormIntent
    
    data object AddFood : EntryFormIntent
}