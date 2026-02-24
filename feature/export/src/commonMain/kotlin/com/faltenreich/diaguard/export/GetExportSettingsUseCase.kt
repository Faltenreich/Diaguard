package com.faltenreich.diaguard.export

import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.data.export.ExportType
import com.faltenreich.diaguard.data.export.PdfLayout
import com.faltenreich.diaguard.data.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.category.usecase.GetActiveMeasurementCategoriesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetExportSettingsUseCase(
    private val getCategories: GetActiveMeasurementCategoriesUseCase,
    private val propertyRepository: MeasurementPropertyRepository,
) {

    operator fun invoke(): Flow<ExportSettings> = getCategories().map { categories ->
        // TODO: Read everything from KeyValueStore
        val exportTypes = ExportType.entries
        val pdfLayouts = PdfLayout.entries

        ExportSettings(
            date = ExportSettings.Date(
                includeCalendarWeek = true,
                includeDateOfExport = true,
            ),
            type = ExportSettings.Type(
                selection = exportTypes.first(),
                options = exportTypes,
            ),
            layout = ExportSettings.Layout(
                selection = pdfLayouts.first(),
                options = pdfLayouts,
                includePageNumber = true,
                includeDaysWithoutEntries = true,
            ),
            content = ExportSettings.Content(
                categories = categories.map { category ->
                    val properties = propertyRepository.getByCategoryId(category.id)
                    ExportSettings.Content.Category(
                        category = category,
                        isExported = true,
                        properties = properties.map { property ->
                            ExportSettings.Content.Category.Property(
                                property = property,
                                isExported = true,
                            )
                        }
                    )
                },
                includeNotes = true,
                includeTags = true,
            ),

        )
    }
}