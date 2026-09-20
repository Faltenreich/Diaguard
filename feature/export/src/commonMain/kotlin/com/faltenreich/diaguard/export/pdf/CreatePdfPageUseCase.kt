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
        val header = PdfHeader(
            calendarWeek = "${localization.getString(Res.string.calendar_week)} ${
                dateTimeFormatter.formatWeek(date)
            }",
            dateRange = dateTimeFormatter.formatDateRange(dateRange),
        ).takeIf { settings.includeCalendarWeek }

        val pageNumber = document.countPages() + 1 // Increment beforehand
        val footer = PdfFooter(
            dateOfExport = localization.getString(
                Res.string.export_date_time,
                dateTimeFormatter.formatDate(date),
            ).takeIf { settings.includeDateOfExport },
            pageNumber = pageNumber.toString().takeIf { settings.includePageNumber },
        ).takeIf { settings.includeDateOfExport || settings.includePageNumber }

        return PdfPage(document, header, footer)
    }
}