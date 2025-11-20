package com.faltenreich.diaguard.permission

import android.Manifest
import android.os.Build

val Permission.code: String?
    get() = when (this) {
        Permission.POST_NOTIFICATIONS ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.POST_NOTIFICATIONS
            else null
        Permission.WRITE_EXTERNAL_STORAGE -> Manifest.permission.WRITE_EXTERNAL_STORAGE
    }

fun Permission.Companion.fromCode(code: String): Permission? {
    return Permission.entries.firstOrNull { it.code == code }
}