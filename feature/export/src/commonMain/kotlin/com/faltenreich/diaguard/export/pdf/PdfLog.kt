package com.faltenreich.diaguard.export.pdf

internal class PdfLog : PdfDrawable {

    override fun getSize(): PdfSize = PdfSize.Zero

    override fun drawOn(page: PdfPage, position: PdfPosition) = Unit
}