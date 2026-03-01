package com.faltenreich.diaguard.export

import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.data.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.export.preference.ExportCategoryPreference
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
import kotlinx.coroutines.flow.map

class GetExportSettingsUseCase(
    private val getCategories: GetActiveMeasurementCategoriesUseCase,
    private val propertyRepository: MeasurementPropertyRepository,
    private val getPreference: GetPreferenceUseCase,
) {

    operator fun invoke(): Flow<ExportSettings> = combine(
        getCategories().flatMapConcat { categories ->
            val collectCategories = categories.map { category ->
                val properties = propertyRepository.getByCategoryId(category.id)
                getPreference(ExportCategoryPreference(category)).map { isExported ->
                    ExportSettings.Category(
                        category = category,
                        isExported = isExported,
                        properties = properties.map { property ->
                            ExportSettings.Category.Property(
                                property = property,
                                isExported = true,
                            )
                        }
                    )
                }
            }
            combine(collectCategories) { categories ->
                categories
            }
        },
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
            categories = categories.toList(),
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
}