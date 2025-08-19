package com.faltenreich.diaguard.shared.permission

data class PermissionResult(
    val permission: Permission,
    val isGranted: Boolean,
)