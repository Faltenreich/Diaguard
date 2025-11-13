package com.faltenreich.diaguard.core.localization

actual fun String.format(vararg arguments: Any?): String {
    return format(args = arguments)
}