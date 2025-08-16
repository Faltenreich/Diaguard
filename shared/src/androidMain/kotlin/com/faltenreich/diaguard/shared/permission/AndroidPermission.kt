package com.faltenreich.diaguard.shared.permission

import android.Manifest

val Permission.code
    get() = when (this) {
        Permission.POST_NOTIFICATIONS -> Manifest.permission.POST_NOTIFICATIONS
        Permission.WRITE_EXTERNAL_STORAGE -> Manifest.permission.WRITE_EXTERNAL_STORAGE
    }