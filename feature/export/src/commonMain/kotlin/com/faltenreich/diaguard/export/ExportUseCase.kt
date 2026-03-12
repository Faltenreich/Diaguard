package com.faltenreich.diaguard.export

import com.faltenreich.diaguard.data.entry.EntryRepository
import com.faltenreich.diaguard.data.entry.tag.EntryTagRepository
import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.data.export.ExportType
import com.faltenreich.diaguard.data.food.eaten.FoodEatenRepository
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.datetime.DateRange
import com.faltenreich.diaguard.export.pdf.PdfExport
import com.faltenreich.diaguard.persistence.file.File

class ExportUseCase(
    private val entryRepository: EntryRepository,
    private val valueRepository: MeasurementValueRepository,
    private val entryTagRepository: EntryTagRepository,
    private val foodEatenRepository: FoodEatenRepository,
    private val pdfExport: PdfExport,
) {

    suspend operator fun invoke(
        dateRange: DateRange,
        settings: ExportSettings,
    ): File? {
        val entries = entryRepository.getByDateRange(
            startDateTime = dateRange.start.atStartOfDay(),
            endDateTime = dateRange.endInclusive.atEndOfDay(),
        ).map { entry ->
            entry.apply {
                values = valueRepository.getByEntryId(id)
                if (settings.includeTags) entryTags = entryTagRepository.getByEntryId(id)
                if (settings.includeFoodEaten) foodEaten = foodEatenRepository.getByEntryId(id)
            }
        }
        return when (settings.exportType) {
            ExportType.PDF -> pdfExport.export(entries, settings)
            ExportType.CSV -> TODO()
        }
    }
}