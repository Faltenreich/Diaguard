package com.faltenreich.diaguard.export.pdf.empty

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.export.pdf.PdfCell
import com.faltenreich.diaguard.export.pdf.PdfEmpty
import com.faltenreich.diaguard.export.pdf.PdfText
import com.faltenreich.diaguard.export.pdf.datetime.PdfDateFactory
import com.faltenreich.diaguard.localization.Localization
import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPaint
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.export_empty

internal class PdfEmptyFactory(
    private val dateFactory: PdfDateFactory,
    private val localization: Localization,
) {

    fun create(
        date: Date,
        width: Float,
    ): PdfDrawable {
        return PdfEmpty(
            width = width,
            date = dateFactory.create(date, width, withHours = false),
            label = PdfCell(
                PdfText(
                    localization.getString(Res.string.export_empty),
                    PdfPaint.label
                )
            ),
        )
    }

    private companion object {

        const val DATE_WIDTH = 100f
    }
}