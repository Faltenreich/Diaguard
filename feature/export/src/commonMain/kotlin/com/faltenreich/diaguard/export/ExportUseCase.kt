package com.faltenreich.diaguard.export

import com.faltenreich.diaguard.data.entry.EntryRepository
import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.data.export.ExportType
import com.faltenreich.diaguard.datetime.DateRange
import com.faltenreich.diaguard.export.pdf.PdfExport
import com.faltenreich.diaguard.persistence.file.File

class ExportUseCase(
    private val entryRepository: EntryRepository,
    private val pdfExport: PdfExport,
) {

    suspend operator fun invoke(
        dateRange: DateRange,
        settings: ExportSettings,
    ): File? {
        val entries = entryRepository.getByDateRange(
            startDateTime = dateRange.start.atStartOfDay(),
            endDateTime = dateRange.endInclusive.atEndOfDay(),
        )
        return when (settings.exportType) {
            ExportType.PDF -> pdfExport.export(entries, settings)
            ExportType.CSV -> TODO()
        }
    }
}