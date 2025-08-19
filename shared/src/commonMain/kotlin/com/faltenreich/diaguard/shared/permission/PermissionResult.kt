package com.faltenreich.diaguard.shared.permission

sealed interface PermissionResult {

    data class Granted(
        val permission: Permission,
    ) : PermissionResult

    data class Denied(
        val permission: Permission,
        val shouldShowRationale: Boolean,
    ) : PermissionResult

    data object Unknown : PermissionResult
}