package com.faltenreich.diaguard.export.preference

import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.data.preference.Preference

data class ExportCategoryPreference(
    private val category: MeasurementCategory.Local,
) : Preference<Boolean, Boolean> {

    override val key = "preference_export_category_" + category.id

    override val default = true

    override val onRead = { value: Boolean -> value }

    override val onWrite = { value: Boolean -> value }
}