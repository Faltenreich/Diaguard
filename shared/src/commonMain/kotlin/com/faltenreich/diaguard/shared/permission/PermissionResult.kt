package com.faltenreich.diaguard.shared.permission

sealed interface PermissionResult {

    data object Granted : PermissionResult

    data object Denied : PermissionResult

    data object Unknown : PermissionResult
}