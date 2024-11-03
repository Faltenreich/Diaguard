package com.faltenreich.diaguard.export

import com.faltenreich.diaguard.export.pdf.PdfExport
import com.faltenreich.diaguard.export.pdf.document

class ExportUseCase(private val exportPdf: PdfExport) {

    operator fun invoke(data: ExportData) {
        when (data) {
            is ExportData.Pdf -> exportPdf()
            is ExportData.Csv -> TODO()
        }
    }

    // TODO: Pass ExportData
    private fun exportPdf() {
        exportPdf(
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