package com.faltenreich.diaguard.shared.clipboard

expect class PlatformClipboard constructor(): Clipboard {

    override fun copyToClipboard(string: String)
}