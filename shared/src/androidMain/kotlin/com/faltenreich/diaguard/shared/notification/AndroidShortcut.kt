package com.faltenreich.diaguard.shared.notification

enum class AndroidShortcut(val action: String) {
    CREATE_ENTRY("com.faltenreich.diaguard.NEW_ENTRY"),
    ;

    companion object {

        fun forAction(action: String): AndroidShortcut? {
            return entries.firstOrNull { it.action == action }
        }
    }
}