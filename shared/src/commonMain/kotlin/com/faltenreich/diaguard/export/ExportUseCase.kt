package com.faltenreich.diaguard.export

import com.faltenreich.diaguard.export.pdf.PdfExport
import com.faltenreich.diaguard.shared.di.inject

class ExportUseCase(
    private val pdfExport: PdfExport = inject(),
) {

    operator fun invoke() {
        pdfExport.export()
    }
}