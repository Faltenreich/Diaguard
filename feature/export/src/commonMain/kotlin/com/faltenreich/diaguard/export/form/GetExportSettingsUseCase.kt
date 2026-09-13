package com.faltenreich.diaguard.export.form

import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.data.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.export.preference.ExportCategoryPreference
import com.faltenreich.diaguard.export.preference.ExportPropertyPreference
import com.faltenreich.diaguard.export.preference.ExportTypePreference
import com.faltenreich.diaguard.export.preference.IncludeCalendarWeekPreference
import com.faltenreich.diaguard.export.preference.IncludeDateOfExportPreference
import com.faltenreich.diaguard.export.preference.IncludeDaysWithoutEntriesPreference
import com.faltenreich.diaguard.export.preference.IncludeFoodEatenPreference
import com.faltenreich.diaguard.export.preference.IncludeNotesPreference
import com.faltenreich.diaguard.export.preference.IncludePageNumberPreference
import com.faltenreich.diaguard.export.preference.IncludeTagsPreference
import com.faltenreich.diaguard.export.preference.PdfLayoutPreference
import com.faltenreich.diaguard.measurement.category.usecase.GetActiveMeasurementCategoriesUseCase
import com.faltenreich.diaguard.preference.GetPreferenceUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf

class GetExportSettingsUseCase(
    private val getActiveCategories: GetActiveMeasurementCategoriesUseCase,
    private val propertyRepository: MeasurementPropertyRepository,
    private val getPreference: GetPreferenceUseCase,
) {

    operator fun invoke(): Flow<ExportSettings> = combine(
        getCategories(),
        getPreference(ExportTypePreference),
        getPreference(IncludeCalendarWeekPreference),
        getPreference(IncludeDateOfExportPreference),
        getPreference(IncludeDaysWithoutEntriesPreference),
        getPreference(IncludeNotesPreference),
        getPreference(IncludePageNumberPreference),
        getPreference(IncludeTagsPreference),
        getPreference(IncludeFoodEatenPreference),
        getPreference(PdfLayoutPreference),
        ::ExportSettings,
    )

    private fun getCategories(): Flow<List<ExportSettings.Category>> {
        return getActiveCategories().flatMapConcat { categories ->
            if (categories.isNotEmpty()) combine(categories.map { it.toSetting() }) { it.toList() }
            else flowOf(emptyList())
        }
    }

    private fun MeasurementCategory.Local.toSetting(): Flow<ExportSettings.Category> {
        return combine(
            flowOf(this),
            getPreference(ExportCategoryPreference(this)),
            propertyRepository.observeByCategoryId(id).flatMapConcat { properties ->
                if (properties.isNotEmpty()) combine(properties.map { it.toSetting() }) { it.toList() }
                else flowOf(emptyList())
            },
            ExportSettings::Category,
        )
    }

    private fun MeasurementProperty.Local.toSetting(): Flow<ExportSettings.Category.Property> {
        return combine(
            flowOf(this),
            getPreference(ExportPropertyPreference(this)),
            ExportSettings.Category::Property,
        )
    }
}