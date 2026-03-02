package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.data.export.ExportSettings

class ExportPdfUseCase(private val pdfExport: PdfExport) {

    operator fun invoke(settings: ExportSettings) {
        pdfExport.export(settings)
    }
}