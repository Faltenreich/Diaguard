package com.faltenreich.rhyme.shared.clipboard

actual class PlatformClipboard: Clipboard {

    actual override fun copyToClipboard(string: String) {
        UIPasteboard.general.string = string
    }
}