package com.faltenreich.diaguard.export.pdf

fun interface PdfExport {

    operator fun invoke(pdfDocument: PdfDocument)
}