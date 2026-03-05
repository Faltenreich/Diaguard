package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.persistence.file.File

class ExportPdfUseCase(private val pdfExport: PdfExport) {

    suspend operator fun invoke(settings: ExportSettings): File {
        return pdfExport.export(settings)
    }
}