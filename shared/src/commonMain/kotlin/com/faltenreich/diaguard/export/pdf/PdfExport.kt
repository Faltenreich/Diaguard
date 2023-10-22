package com.faltenreich.diaguard.export.pdf

class PdfExport(
    private val pdfRepository: PdfRepository,
) {

    fun export() {
        val pdfDocument = pdf {
            page {

            }
        }
        pdfRepository.export(pdfDocument)
    }
}