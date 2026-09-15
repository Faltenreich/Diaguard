package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.data.export.ExportType
import com.faltenreich.diaguard.data.export.PdfLayout
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateProgression
import com.faltenreich.diaguard.datetime.DateRange
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.localization.Localization
import com.faltenreich.diaguard.logging.Logger
import com.faltenreich.diaguard.persistence.file.File
import com.faltenreich.diaguard.persistence.file.FileRepository
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.calendar_week
import com.faltenreich.diaguard.resource.export_date_time
import com.faltenreich.diaguard.resource.export_empty
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class ExportPdfUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val localization: Localization,
    private val fileRepository: FileRepository,
    private val dateTimeFactory: DateTimeFactory,
    private val dateTimeFormatter: DateTimeFormatter,
) {

    suspend operator fun invoke(
        dateRange: DateRange,
        entries: List<Entry>,
        settings: ExportSettings,
    ): File? = withContext(dispatcher) {
        try {
            val now = dateTimeFactory.now()
            val nowLocalized = dateTimeFormatter.formatDateTime(
                now,
                EXPORT_DATE_TIME_FORMAT,
            )
            val prefix = EXPORT_FILE_NAME_PREFIX
            val extension = ExportType.PDF.extension
            val fileName = "${prefix}_$nowLocalized.$extension"

            val file = fileRepository.createDocument(fileName, MIME_TYPE_PDF)
                ?: return@withContext null

            val pdfDocument = PdfDocument(file)
            var page = createPage(pdfDocument, dateRange.start, settings)

            DateProgression(dateRange).forEachIndexed { index, date ->
                val isNewPage = index != 0 &&
                    date == dateTimeFactory.dateAtStartOf(date, DateUnit.WEEK)
                if (isNewPage) {
                    page = createPage(pdfDocument, date, settings)
                }

                val entriesOfDate = entries.filter { it.dateTime == date }
                // TODO: Outsource in component, add padding and hours
                val day = PdfText(
                    text = dateTimeFormatter.formatDate(date),
                    paint = PdfPaint.bold,
                )
                val dayHeight = day.getSize(page).height

                val content = when {
                    entriesOfDate.isNotEmpty() -> when (settings.pdfLayout) {
                        PdfLayout.LOG -> PdfLog()
                        PdfLayout.TABLE -> PdfTable(date, entries)
                        PdfLayout.TIMELINE -> PdfTimeline()
                    }

                    settings.includeDaysWithoutEntries -> PdfEmpty(
                        text = localization.getString(Res.string.export_empty),
                    )

                    else -> return@forEachIndexed
                }
                val contentHeight = content.getSize(page).height

                if (!page.canMove(dayHeight + contentHeight)) {
                    page.finish()
                    page = createPage(pdfDocument, date, settings)
                }

                day.drawOn(page, page.offset)
                page.move(dayHeight)

                content.drawOn(page, page.offset)
                page.move(contentHeight)
            }

            page.finish()
            pdfDocument.close()

            file
        } catch (exception: Exception) {
            Logger.error("Export failed", exception)
            null
        }
    }

    private fun createPage(
        document: PdfDocument,
        date: Date,
        settings: ExportSettings,
    ): PdfPage {
        val dateRange = DateRange(
            dateTimeFactory.dateAtStartOf(date, DateUnit.WEEK),
            dateTimeFactory.dateAtEndOf(date, DateUnit.WEEK),
        )
        val header = PdfHeader(
            calendarWeek = PdfText(
                text = "${localization.getString(Res.string.calendar_week)} ${
                    dateTimeFormatter.formatWeek(date)
                }",
                paint = PdfPaint.header,
            ),
            dateRange = PdfText(
                text = dateTimeFormatter.formatDateRange(dateRange),
                paint = PdfPaint.normal,
            )
        ).takeIf { settings.includeCalendarWeek }

        val pageNumber = document.countPages() + 1 // Increment beforehand
        val footer = PdfFooter(
            dateOfExport = PdfText(
                text = localization.getString(
                    Res.string.export_date_time,
                    dateTimeFormatter.formatDate(date),
                ),
                paint = PdfPaint.normal,
            ).takeIf { settings.includeDateOfExport },
            pageNumber = PdfText(
                text = pageNumber.toString(),
                paint = PdfPaint.normal,
            ).takeIf { settings.includePageNumber },
        ).takeIf { settings.includeDateOfExport || settings.includePageNumber }

        return PdfPage(document, header, footer)
    }

    companion object {

        private const val EXPORT_FILE_NAME_PREFIX = "Diaguard"
        private const val EXPORT_DATE_TIME_FORMAT = "yyyy-MM-dd_HH-mm"
        private const val MIME_TYPE_PDF = "application/pdf"
    }
}