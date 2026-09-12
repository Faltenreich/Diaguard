package com.faltenreich.diaguard.export.pdf

import android.content.Context
import android.os.Environment
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
import com.faltenreich.diaguard.export.pdf.print.Pdf
import com.faltenreich.diaguard.export.pdf.print.PdfFooter
import com.faltenreich.diaguard.export.pdf.print.PdfHeader
import com.faltenreich.diaguard.export.pdf.print.PdfLog
import com.faltenreich.diaguard.export.pdf.print.PdfPage
import com.faltenreich.diaguard.export.pdf.print.PdfPaint
import com.faltenreich.diaguard.export.pdf.print.PdfTable
import com.faltenreich.diaguard.export.pdf.print.PdfText
import com.faltenreich.diaguard.export.pdf.print.PdfTimeline
import com.faltenreich.diaguard.localization.Localization
import com.faltenreich.diaguard.logging.Logger
import com.faltenreich.diaguard.persistence.file.File
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.calendar_week
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File as JavaFile

// TODO: Merge with ExportUseCase and make platform-agnostic
class AndroidPdfExport(
    private val dispatcher: CoroutineDispatcher,
    private val context: Context,
    private val localization: Localization,
    private val dateTimeFactory: DateTimeFactory,
    private val dateTimeFormatter: DateTimeFormatter,
) : PdfExport {

    override suspend fun export(
        dateRange: DateRange,
        entries: List<Entry.Local>,
        settings: ExportSettings,
    ): File? = withContext(dispatcher) {
        try {
            val now = dateTimeFactory.now()
            val nowLocalized = dateTimeFormatter.formatDateTime(
                now,
                EXPORT_DATE_TIME_FORMAT,
            )
            val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            val prefix = EXPORT_FILE_NAME_PREFIX
            val extension = ExportType.PDF.extension
            val fileName = "${prefix}_$nowLocalized.$extension"
            val file = JavaFile(directory, fileName)

            val pdf = Pdf()
            pdf.open(file)

            DateProgression(dateRange).forEachIndexed { index, date ->
                val isNewPage = index == 0 ||
                    date == dateTimeFactory.dateAtStartOf(date, DateUnit.WEEK)
                if (isNewPage) {
                    createPage(pdf, date, settings)
                }

                val entriesOfDate = entries.filter { it.dateTime == date }
                val exportDay = entriesOfDate.isNotEmpty() || settings.includeDaysWithoutEntries
                if (exportDay) {
                    val day = when (settings.pdfLayout) {
                        PdfLayout.LOG -> PdfLog()
                        PdfLayout.TABLE -> PdfTable()
                        PdfLayout.TIMELINE -> PdfTimeline()
                    }

                    val height = day.getSize().height
                    if (pdf.canMove(height)) {
                        pdf.move(height)
                    } else {
                        pdf.finishPage()
                        createPage(pdf, date, settings)
                    }

                    pdf.draw(day)
                }
            }

            pdf.finishPage()
            pdf.close()

            File(
                absolutePath = file.absolutePath,
                createdAt = now,
                mimeType = MIME_TYPE_PDF,
            )
        } catch (exception: Exception) {
            Logger.error("Export failed", exception)
            null
        }
    }

    private fun createPage(
        pdf: Pdf,
        date: Date,
        settings: ExportSettings,
    ) {
        val header = PdfHeader(
            calendarWeek = PdfText(
                text = "%s %s".format(
                    localization.getString(Res.string.calendar_week),
                    dateTimeFormatter.formatWeek(date),
                ),
                paint = PdfPaint.header,
            ),
            dateRange = PdfText(
                text = dateTimeFormatter.formatDate(date), // TODO: Range
                paint = PdfPaint.normal,
            )
        ).takeIf { settings.includeCalendarWeek }

        val footer = PdfFooter(
            dateOfExport = PdfText(
                text = dateTimeFormatter.formatDate(date), // TODO
                paint = PdfPaint.normal,
            ).takeIf { settings.includeDateOfExport },
            pageNumber = PdfText(
                text = 0.toString(), // TODO
                paint = PdfPaint.normal,
            ).takeIf { settings.includePageNumber },
        ).takeIf { settings.includeDateOfExport || settings.includePageNumber }

        pdf.addPage(PdfPage(header, footer))
    }

    companion object {

        private const val EXPORT_FILE_NAME_PREFIX = "Diaguard"
        private const val EXPORT_DATE_TIME_FORMAT = "yyyy-MM-dd_HH-mm"
        private const val MIME_TYPE_PDF = "application/pdf"
    }
}