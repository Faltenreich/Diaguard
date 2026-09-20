package com.faltenreich.diaguard.persistence.pdf

interface PdfDrawable {

    fun getSize(): PdfSize

    fun drawOn(page: PdfPage, position: PdfPosition)
}