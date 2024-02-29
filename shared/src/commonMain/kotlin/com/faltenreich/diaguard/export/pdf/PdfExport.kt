package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.export.ExportData
import com.faltenreich.diaguard.shared.logging.Logger

class PdfExport(
    private val pdfRepository: PdfRepository,
) {

    fun export(data: ExportData.Pdf) {
        Logger.debug("Export data: $data")
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