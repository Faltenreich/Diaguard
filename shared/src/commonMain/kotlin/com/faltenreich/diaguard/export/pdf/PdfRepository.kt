package com.faltenreich.diaguard.export.pdf

expect class PdfRepository constructor() {

    fun export(pdfDocument: PdfDocument)
}