package com.faltenreich.diaguard.system.web

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

class AndroidUrlOpener(private val context: Context) : UrlOpener {

    override fun open(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}