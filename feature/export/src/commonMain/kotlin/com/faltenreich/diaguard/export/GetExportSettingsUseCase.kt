package com.faltenreich.diaguard.export

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
import kotlinx.coroutines.flow.map

class GetExportSettingsUseCase(
    private val getActiveCategories: GetActiveMeasurementCategoriesUseCase,
    private val propertyRepository: MeasurementPropertyRepository,
    private val getPreference: GetPreferenceUseCase,
) {

    operator fun invoke(): Flow<ExportSettings> = combine(
        getCategories(),
        getPreference(ExportTypePreference),
        getPreference(PdfLayoutPreference),
        getPreference(IncludeCalendarWeekPreference),
        getPreference(IncludeDateOfExportPreference),
        getPreference(IncludeDaysWithoutEntriesPreference),
        getPreference(IncludePageNumberPreference),
        getPreference(IncludeNotesPreference),
        getPreference(IncludeTagsPreference),
    ) {
        categories,
        exportType,
        pdfLayout,
        includeCalendarWeek,
        includeDateOfExport,
        includeDaysWithoutEntries,
        includePageNumber,
        includeNotes,
        includeTags,
        ->
        ExportSettings(
            categories = categories,
            exportType = exportType,
            includeCalendarWeek = includeCalendarWeek,
            includeDateOfExport = includeDateOfExport,
            includeDaysWithoutEntries = includeDaysWithoutEntries,
            includePageNumber = includePageNumber,
            includeNotes = includeNotes,
            includeTags = includeTags,
            pdfLayout = pdfLayout,
        )
    }

    private fun getCategories(): Flow<List<ExportSettings.Category>> {
        return getActiveCategories().flatMapConcat { categories ->
            if (categories.isNotEmpty()) combine(categories.map { it.toSetting() }) { it.toList() }
            else flowOf(emptyList())
        }
    }

    private fun MeasurementCategory.Local.toSetting(): Flow<ExportSettings.Category> {
        return combine(
            getPreference(ExportCategoryPreference(this)),
            propertyRepository.observeByCategoryId(id).flatMapConcat { properties ->
                if (properties.isNotEmpty()) combine(properties.map { it.toSetting() }) { it.toList() }
                else flowOf(emptyList())
            },
        ) { isExported, properties ->
            ExportSettings.Category(
                category = this,
                isExported = isExported,
                properties = properties,
            )
        }
    }

    private fun MeasurementProperty.Local.toSetting(): Flow<ExportSettings.Category.Property> {
        return getPreference(ExportPropertyPreference(this)).map { isExported ->
            ExportSettings.Category.Property(
                property = this,
                isExported = isExported,
            )
        }
    }
}