package com.faltenreich.diaguard.shared.networking

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.faltenreich.diaguard.shared.di.inject

actual class UrlOpener {

    actual fun open(url: String) {
        val context = inject<Context>()
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}