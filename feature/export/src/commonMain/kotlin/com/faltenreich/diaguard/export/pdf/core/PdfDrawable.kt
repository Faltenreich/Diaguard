package com.faltenreich.diaguard.export.pdf.core

internal interface PdfDrawable {

    fun getSize(): PdfSize

    fun drawOn(page: PdfPage, position: PdfPosition)
}