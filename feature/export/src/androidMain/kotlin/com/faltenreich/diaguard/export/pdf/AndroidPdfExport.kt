package com.faltenreich.diaguard.export.pdf

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.Environment
import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.data.export.ExportType
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.persistence.file.File
import java.io.FileOutputStream
import android.graphics.pdf.PdfDocument as AndroidPdfDocument
import java.io.File as JavaFile

class AndroidPdfExport(
    private val context: Context,
    private val dateTimeFactory: DateTimeFactory,
    private val dateTimeFormatter: DateTimeFormatter,
) : PdfExport {

    override suspend fun export(settings: ExportSettings): File {
        val dateTime = dateTimeFactory.now()
        val dateTimeFormatted = dateTimeFormatter.formatDateTime(dateTime, EXPORT_DATE_TIME_FORMAT)
        val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val prefix = EXPORT_FILE_NAME_PREFIX
        val extension = ExportType.PDF.extension
        val fileName = "${prefix}_$dateTimeFormatted.$extension"
        val file = JavaFile(directory, fileName)

        val outputStream = FileOutputStream(file)
        val document = AndroidPdfDocument()

        val pageNumber = 1
        val pageInfo =
            AndroidPdfDocument.PageInfo.Builder(PDF_PAGE_WIDTH, PDF_PAGE_HEIGHT, pageNumber)
                .create()
        val page = document.startPage(pageInfo)

        // TODO: Pass content of PdfDocument
        page.canvas.drawText("Hello, World!", 100f, 100f, Paint().apply { color = Color.BLACK })

        document.finishPage(page)
        document.writeTo(outputStream)
        document.close()

        return File(
            absolutePath = file.absolutePath,
            createdAt = dateTime,
            mimeType = MIME_TYPE_PDF,
        )
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