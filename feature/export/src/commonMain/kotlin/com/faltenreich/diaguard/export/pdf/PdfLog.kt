package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.export.pdf.core.PdfDrawable
import com.faltenreich.diaguard.export.pdf.core.PdfPage
import com.faltenreich.diaguard.export.pdf.core.PdfPosition
import com.faltenreich.diaguard.export.pdf.core.PdfSize

internal class PdfLog : PdfDrawable {

    override fun getSize(): PdfSize = PdfSize.Zero

    override fun drawOn(page: PdfPage, position: PdfPosition) = Unit
}