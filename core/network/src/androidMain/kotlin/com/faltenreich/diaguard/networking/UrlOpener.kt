package com.faltenreich.diaguard.networking

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.faltenreich.diaguard.injection.inject

actual class UrlOpener {

    actual fun open(url: String) {
        val context = inject<Context>()
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}