package com.faltenreich.diaguard.export.pdf.timeline

import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfSize

internal class PdfTimelineChart : PdfDrawable {

    override fun getSize(): PdfSize {
        return PdfSize.Zero
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {

    }
}