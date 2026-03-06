package com.faltenreich.diaguard.export

import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.data.export.ExportType
import com.faltenreich.diaguard.export.pdf.PdfExport
import com.faltenreich.diaguard.persistence.file.File

class ExportUseCase(private val pdfExport: PdfExport) {

    suspend operator fun invoke(settings: ExportSettings): File? {
        return when (settings.exportType) {
            ExportType.PDF -> pdfExport.export(settings)
            ExportType.CSV -> TODO()
        }
    }
}