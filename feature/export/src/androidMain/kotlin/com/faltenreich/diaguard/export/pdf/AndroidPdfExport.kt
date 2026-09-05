package com.faltenreich.diaguard.export.pdf

import android.content.Context
import android.os.Environment
import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.data.export.ExportType
import com.faltenreich.diaguard.datetime.DateRange
import com.faltenreich.diaguard.datetime.DateRangeProgression
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.export.pdf.print.Pdf
import com.faltenreich.diaguard.export.pdf.print.PdfPaint
import com.faltenreich.diaguard.export.pdf.print.PdfText
import com.faltenreich.diaguard.localization.Localization
import com.faltenreich.diaguard.logging.Logger
import com.faltenreich.diaguard.persistence.file.File
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.calendar_week
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File as JavaFile

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
            val dateTime = dateTimeFactory.now()
            val dateTimeFormatted = dateTimeFormatter.formatDateTime(
                dateTime,
                EXPORT_DATE_TIME_FORMAT,
            )
            val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            val prefix = EXPORT_FILE_NAME_PREFIX
            val extension = ExportType.PDF.extension
            val fileName = "${prefix}_$dateTimeFormatted.$extension"
            val file = JavaFile(directory, fileName)

            val pdf = Pdf()
            pdf.open(file)
            pdf.addPage()

            if (settings.includeCalendarWeek) {
                val title = PdfText(
                    text = "%s %s".format(
                        localization.getString(Res.string.calendar_week),
                        dateTimeFormatter.formatWeek(dateTime.date),
                    ),
                    paint = PdfPaint.header,
                )
                pdf.draw(title)
                pdf.moveY(title.getSize().height)

                val subtitle = PdfText(
                    text = dateTimeFormatter.formatDate(dateTime.date),
                    paint = PdfPaint.normal,
                )
                pdf.draw(subtitle)
                pdf.moveY(subtitle.getSize().height)
            }

            for (date in DateRangeProgression(dateRange)) {
                val entriesOfDate = entries.filter { it.dateTime == date }
                val exportDay = entriesOfDate.isNotEmpty() || settings.includeDaysWithoutEntries
                if (exportDay) {

                }
            }

            pdf.closePage()
            pdf.close()

            File(
                absolutePath = file.absolutePath,
                createdAt = dateTime,
                mimeType = MIME_TYPE_PDF,
            )
        } catch (exception: Exception) {
            Logger.error("Export failed", exception)
            null
        }
    }

    companion object {

        private const val EXPORT_FILE_NAME_PREFIX = "Diaguard"
        private const val EXPORT_DATE_TIME_FORMAT = "yyyy-MM-dd_HH-mm"
        private const val MIME_TYPE_PDF = "application/pdf"
    }
}