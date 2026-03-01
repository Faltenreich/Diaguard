package com.faltenreich.diaguard.export

import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.data.export.PdfLayout
import com.faltenreich.diaguard.data.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.export.preference.ExportTypePreference
import com.faltenreich.diaguard.measurement.category.usecase.GetActiveMeasurementCategoriesUseCase
import com.faltenreich.diaguard.preference.GetPreferenceUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetExportSettingsUseCase(
    private val getCategories: GetActiveMeasurementCategoriesUseCase,
    private val propertyRepository: MeasurementPropertyRepository,
    private val getPreference: GetPreferenceUseCase,
) {

    operator fun invoke(): Flow<ExportSettings> = combine(
        getCategories(),
        getPreference(ExportTypePreference),
    ) { categories, exportType ->
        // TODO: Read everything from KeyValueStore
        val pdfLayouts = PdfLayout.entries

        ExportSettings(
            categories = categories.map { category ->
                val properties = propertyRepository.getByCategoryId(category.id)
                ExportSettings.Category(
                    category = category,
                    isExported = true,
                    properties = properties.map { property ->
                        ExportSettings.Category.Property(
                            property = property,
                            isExported = true,
                        )
                    }
                )
            },
            exportType = exportType,
            includeCalendarWeek = true,
            includeDateOfExport = true,
            includeDaysWithoutEntries = true,
            includePageNumber = true,
            includeNotes = true,
            includeTags = true,
            pdfLayout = pdfLayouts.first(),
        )
    }
}