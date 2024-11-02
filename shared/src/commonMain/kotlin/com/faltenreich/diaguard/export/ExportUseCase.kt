package com.faltenreich.diaguard.export

import com.faltenreich.diaguard.export.pdf.PdfExport
import com.faltenreich.diaguard.export.pdf.document

class ExportUseCase(private val pdfExport: PdfExport) {

    operator fun invoke(data: ExportData) {
        when (data) {
            is ExportData.Pdf -> exportPdf(data)
            is ExportData.Csv -> TODO()
        }
    }

    private fun exportPdf(data: ExportData) {
        pdfExport.export(
            document {
                page {
                    header {

                    }
                    footer {

                    }
                }
            }
        )
    }
}