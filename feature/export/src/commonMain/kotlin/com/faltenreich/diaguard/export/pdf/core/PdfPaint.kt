package com.faltenreich.diaguard.export.pdf.core

internal expect class PdfPaint {

    fun getTextBounds(text: String): PdfSize

    companion object {

        val normal: PdfPaint
        val label: PdfPaint
        val bold: PdfPaint
        val header: PdfPaint
        val background: PdfPaint
    }
}