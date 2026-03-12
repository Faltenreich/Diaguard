package com.faltenreich.diaguard.persistence.file

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import androidx.core.content.FileProvider

class AndroidFileHandler(private val context: Context) : FileHandler {

    override fun open(file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = getUriForFile(context, file)
        intent.setDataAndType(uri, "application/pdf")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        grantUriPermission(uri, intent, context)
        context.startActivity(intent)
    }

    override fun share(file: File) {
        val intent = Intent.createChooser(
            Intent(Intent.ACTION_SEND).apply {
                setType(file.mimeType)
                putExtra(
                    Intent.EXTRA_STREAM,
                    getUriForFile(context, file),
                )
            },
            null,
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    private fun getSupportingApps(intent: Intent, context: Context): List<ResolveInfo> {
        return context.packageManager.queryIntentActivities(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        )
    }

    private fun getUriForFile(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            java.io.File(file.absolutePath),
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
}