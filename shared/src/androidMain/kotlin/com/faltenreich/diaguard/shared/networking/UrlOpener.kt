package com.faltenreich.diaguard.shared.networking

import android.content.Context
import android.content.Intent
import android.net.Uri

actual class UrlOpener(private val context: Context) {

    actual fun open(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}