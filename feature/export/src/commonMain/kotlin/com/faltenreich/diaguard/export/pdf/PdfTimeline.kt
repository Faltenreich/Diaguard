package com.faltenreich.diaguard.export.pdf

internal class PdfTimeline : PdfDrawable {

    override fun getSize(): PdfSize = PdfSize.Zero

    override fun drawOn(page: PdfPage, position: PdfPosition) = Unit
}