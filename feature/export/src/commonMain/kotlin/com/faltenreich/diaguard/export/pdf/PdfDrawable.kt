package com.faltenreich.diaguard.export.pdf

internal interface PdfDrawable {

    fun getSize(page: PdfPage): PdfSize

    fun drawOn(page: PdfPage, position: PdfPosition)
}