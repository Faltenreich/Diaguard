package com.faltenreich.diaguard.shared.clipboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.faltenreich.diaguard.shared.di.inject

actual class PlatformClipboard: Clipboard {

    actual override fun copyToClipboard(string: String) {
        val context = inject<Context>()
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("", string)
        clipboard.setPrimaryClip(clip)
    }
}