package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.export.ExportData
import com.faltenreich.diaguard.shared.logging.Logger

class PdfExport(private val repository: PdfRepository) {

    fun export(data: ExportData.Pdf) {
        Logger.debug("Export data: $data")
        repository.export(
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