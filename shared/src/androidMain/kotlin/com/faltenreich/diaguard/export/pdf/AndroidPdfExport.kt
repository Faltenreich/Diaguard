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
import java.io.File
import java.io.FileOutputStream
import android.graphics.pdf.PdfDocument as AndroidPdfDocument

class AndroidPdfExport(private val context: Context) : PdfExport {

    override operator fun invoke(pdfDocument: PdfDocument) {
        val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val file = File(directory, "export.pdf")

        val outputStream = FileOutputStream(file)
        val document = AndroidPdfDocument()

        val pageNumber = 1
        val pageInfo = AndroidPdfDocument.PageInfo.Builder(PDF_PAGE_WIDTH, PDF_PAGE_HEIGHT, pageNumber).create()
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
    }

    private fun getSupportingApps(intent: Intent, context: Context): List<ResolveInfo> {
        return context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
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

    private fun getUriForFile(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", file)
    }

    companion object {

        // DIN A4
        private const val PDF_PAGE_WIDTH = 595
        private const val PDF_PAGE_HEIGHT = 842
    }
}