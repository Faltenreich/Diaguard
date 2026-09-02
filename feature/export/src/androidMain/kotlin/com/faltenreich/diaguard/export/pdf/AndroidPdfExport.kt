package com.faltenreich.diaguard.export.pdf

import android.content.Context
import android.os.Environment
import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.data.export.ExportType
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.export.pdf.print.PdfHeader
import com.faltenreich.diaguard.logging.Logger
import com.faltenreich.diaguard.persistence.file.File
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import android.graphics.pdf.PdfDocument as AndroidPdfDocument
import java.io.File as JavaFile

class AndroidPdfExport(
    private val dispatcher: CoroutineDispatcher,
    private val context: Context,
    private val dateTimeFactory: DateTimeFactory,
    private val dateTimeFormatter: DateTimeFormatter,
) : PdfExport {

    override suspend fun export(
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

            val outputStream = FileOutputStream(file)
            val document = AndroidPdfDocument()

            val pageNumber = 1
            val pageInfo = AndroidPdfDocument.PageInfo.Builder(
                PDF_PAGE_WIDTH,
                PDF_PAGE_HEIGHT,
                pageNumber,
            ).create()
            val page = document.startPage(pageInfo)

            if (settings.includeCalendarWeek) {
                PdfHeader(dateTime, dateTimeFormatter).drawOn(page.canvas)
            }

            document.finishPage(page)
            document.writeTo(outputStream)
            document.close()

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

        // DIN A4
        private const val PDF_PAGE_WIDTH = 595
        private const val PDF_PAGE_HEIGHT = 842
    }
}