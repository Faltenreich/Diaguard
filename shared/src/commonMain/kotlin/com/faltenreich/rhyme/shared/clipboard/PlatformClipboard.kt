package com.faltenreich.rhyme.shared.clipboard

expect class PlatformClipboard constructor(): Clipboard {

    override fun copyToClipboard(string: String)
}