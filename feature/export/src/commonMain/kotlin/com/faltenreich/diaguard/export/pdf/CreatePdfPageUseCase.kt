package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateRange
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.localization.Localization
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.calendar_week
import com.faltenreich.diaguard.resource.export_date_time

internal class CreatePdfPageUseCase(
    private val localization: Localization,
    private val dateTimeFactory: DateTimeFactory,
    private val dateTimeFormatter: DateTimeFormatter,
) {

    operator fun invoke(
        document: PdfDocument,
        date: Date,
        settings: ExportSettings,
    ): PdfPage {
        val dateRange = DateRange(
            dateTimeFactory.dateAtStartOf(date, DateUnit.WEEK),
            dateTimeFactory.dateAtEndOf(date, DateUnit.WEEK),
        )

        val size = DIN_A4

        var viewport = PdfRectangle(
            PAGE_PADDING,
            PAGE_PADDING,
            size.width - PAGE_PADDING,
            size.height - PAGE_PADDING,
        )

        val header = PdfHeader(
            calendarWeek = "${localization.getString(Res.string.calendar_week)} ${
                dateTimeFormatter.formatWeek(date)
            }",
            dateRange = dateTimeFormatter.formatDateRange(dateRange),
        ).takeIf { settings.includeCalendarWeek }?.let { header ->
            val height = header.getSize().height
            val position = PdfPosition(x = viewport.left, y = viewport.top)
            viewport = viewport.copy(top = position.y + height)
            header to position
        }

        val pageNumber = document.countPages() + 1 // Increment beforehand
        val footer = PdfFooter(
            dateOfExport = localization.getString(
                Res.string.export_date_time,
                dateTimeFormatter.formatDate(date),
            ).takeIf { settings.includeDateOfExport },
            pageNumber = pageNumber.toString().takeIf { settings.includePageNumber },
        ).takeIf { settings.includeDateOfExport || settings.includePageNumber }?.let { footer ->
            val height = footer.getSize().height
            val position = PdfPosition(x = viewport.left, y = viewport.bottom - height)
            viewport = viewport.copy(bottom = position.y)
            footer to position
        }

        val page = PdfPage(document, size, viewport)
        header?.let { (header, position) ->
            header.drawOn(page, position)
        }
        footer?.let { (footer, position) ->
            footer.drawOn(page, position)
        }

        return page
    }

    private companion object {

        private val DIN_A4 = PdfSize(595f, 842f)
        private const val PAGE_PADDING = 60f
    }
}