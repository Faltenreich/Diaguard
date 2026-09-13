package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.datetime.Date

internal class PdfTable(
    private val date: Date,
    private val entries: List<Entry>,
) : PdfDrawable {

    override fun getSize(page: PdfPage): PdfSize = PdfSize.Zero

    override fun drawOn(page: PdfPage, position: PdfPosition) = Unit
}