package com.faltenreich.diaguard.export.pdf

internal class PdfTable : PdfDrawable {

    override fun getSize(page: PdfPage): PdfSize = PdfSize.Zero

    override fun drawOn(page: PdfPage, position: PdfPosition) = Unit
}