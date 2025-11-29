package com.faltenreich.diaguard.shared.localization

actual fun String.format(vararg arguments: Any?): String {
    return format(args = arguments)
}