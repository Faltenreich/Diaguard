package com.faltenreich.diaguard.export.pdf

internal interface PdfDrawable {

    fun getSize(): PdfSize

    fun drawOn(page: PdfPage, position: PdfPosition)
}