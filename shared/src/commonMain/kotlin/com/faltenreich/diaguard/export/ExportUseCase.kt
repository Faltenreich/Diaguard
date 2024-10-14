package com.faltenreich.diaguard.export

import com.faltenreich.diaguard.export.pdf.PdfExport

class ExportUseCase(
    private val pdfExport: PdfExport,
) {

    operator fun invoke(data: ExportData) {
        when (data) {
            is ExportData.Pdf -> pdfExport.export(data)
            is ExportData.Csv -> TODO()
        }
    }
}