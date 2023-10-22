package com.faltenreich.diaguard.export.pdf

class PdfExport(
    private val pdfRepository: PdfRepository,
) {

    fun export() {
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