package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.export.ExportData

class PdfExport(
    private val pdfRepository: PdfRepository,
) {

    fun export(data: ExportData.Pdf) {
        println(data)
        pdfRepository.export(
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