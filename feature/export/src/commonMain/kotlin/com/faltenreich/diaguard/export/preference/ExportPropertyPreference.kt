package com.faltenreich.diaguard.export.preference

import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.data.preference.Preference

data class ExportPropertyPreference(
    private val property: MeasurementProperty.Local,
) : Preference<Boolean, Boolean> {

    override val key = "preference_export_property_" + property.id

    override val default = true

    override val onRead = { value: Boolean -> value }

    override val onWrite = { value: Boolean -> value }
}