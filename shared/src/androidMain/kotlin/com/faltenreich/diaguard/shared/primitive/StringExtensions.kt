package com.faltenreich.diaguard.shared.primitive

actual fun String.format(vararg arguments: Any?): String {
    return format(args = arguments)
}