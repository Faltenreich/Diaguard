package com.faltenreich.diaguard.export.form

import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.export.preference.ExportCategoryPreference
import com.faltenreich.diaguard.export.preference.ExportPropertyPreference
import com.faltenreich.diaguard.preference.SetPreferenceUseCase

class SetExportCategoryUseCase(
    private val setPreference: SetPreferenceUseCase,
) {

    suspend operator fun invoke(category: ExportSettings.Category) {
        setPreference(ExportCategoryPreference(category.category), category.isExported)
        category.properties.forEach { property ->
            setPreference(ExportPropertyPreference(property.property), property.isExported)
        }
    }
}