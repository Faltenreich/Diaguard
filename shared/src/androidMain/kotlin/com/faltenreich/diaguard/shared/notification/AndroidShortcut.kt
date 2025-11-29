package com.faltenreich.diaguard.shared.notification

val Shortcut.action: String
    get() = when (this) {
        Shortcut.CREATE_ENTRY -> "com.faltenreich.diaguard.NEW_ENTRY"
    }

fun Shortcut.Companion.forAction(action: String): Shortcut? {
    return Shortcut.entries.firstOrNull { it.action == action }
}