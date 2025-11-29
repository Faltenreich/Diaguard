package com.faltenreich.diaguard.export

import com.faltenreich.diaguard.export.pdf.PdfExport
import com.faltenreich.diaguard.export.pdf.document

class ExportUseCase(private val exportPdf: PdfExport) {

    operator fun invoke(data: ExportData) {
        when (data) {
            // TODO: Use ExportData
            is ExportData.Pdf -> exportPdf(
                document {
                    page {
                        header {

                        }
                        footer {

                        }
                    }
                }
            )
            is ExportData.Csv -> TODO()
        }
    }
}