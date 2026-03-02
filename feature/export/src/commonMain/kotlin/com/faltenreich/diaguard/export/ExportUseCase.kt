package com.faltenreich.diaguard.export

import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.data.export.ExportType
import com.faltenreich.diaguard.export.pdf.ExportPdfUseCase

class ExportUseCase(private val exportPdf: ExportPdfUseCase) {

    operator fun invoke(settings: ExportSettings) {
        when (settings.exportType) {
            ExportType.PDF -> exportPdf(settings)
            ExportType.CSV -> TODO()
        }
    }
}