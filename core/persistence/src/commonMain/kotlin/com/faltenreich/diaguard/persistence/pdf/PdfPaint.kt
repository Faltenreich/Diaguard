package com.faltenreich.diaguard.persistence.pdf

expect class PdfPaint {

    fun getTextBounds(text: String): PdfSize

    companion object {

        val normal: PdfPaint
        val label: PdfPaint
        val bold: PdfPaint
        val header: PdfPaint
        val background: PdfPaint
    }
}