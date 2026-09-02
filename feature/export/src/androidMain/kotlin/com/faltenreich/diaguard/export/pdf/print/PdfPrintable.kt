package com.faltenreich.diaguard.export.pdf.print

internal interface PdfPrintable {

    fun draw(state: PdfState)
}