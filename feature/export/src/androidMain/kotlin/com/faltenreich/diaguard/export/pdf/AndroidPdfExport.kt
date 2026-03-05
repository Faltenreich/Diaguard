package com.faltenreich.diaguard.export.pdf

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.data.export.ExportType
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.persistence.file.File
import java.io.FileOutputStream
import android.graphics.pdf.PdfDocument as AndroidPdfDocument
import java.io.File as JavaFile

class AndroidPdfExport(
    private val context: Context,
    private val dateTimeFactory: DateTimeFactory,
) : PdfExport {

    override suspend fun export(settings: ExportSettings): File {
        val dateTime = dateTimeFactory.now()
        // FIXME: Use legacy format: yyyy-MM-dd_HH-mm
        val dateTimeFormatted = dateTime.isoString
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

        val intent = Intent(Intent.ACTION_VIEW)
        val uri = getUriForFile(context, file)
        intent.setDataAndType(uri, "application/pdf")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        grantUriPermission(uri, intent, context)
        context.startActivity(intent)

        return File(
            absolutePath = file.absolutePath,
            createdAt = dateTime,
        )
    }

    private fun getSupportingApps(intent: Intent, context: Context): List<ResolveInfo> {
        return context.packageManager.queryIntentActivities(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        )
    }

    private fun grantUriPermission(uri: Uri, intent: Intent, context: Context) {
        getSupportingApps(intent, context).forEach { resolveInfo ->
            val packageName = resolveInfo.activityInfo.packageName
            context.grantUriPermission(
                packageName,
                uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
    }

    private fun getUriForFile(context: Context, file: JavaFile): Uri {
        return FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            file
        )
    }

    companion object {

        private const val EXPORT_FILE_NAME_PREFIX = "Diaguard"

        // DIN A4
        private const val PDF_PAGE_WIDTH = 595
        private const val PDF_PAGE_HEIGHT = 842
    }
}