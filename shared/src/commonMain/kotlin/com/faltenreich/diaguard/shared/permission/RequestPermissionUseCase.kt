package com.faltenreich.diaguard.shared.permission

class RequestPermissionUseCase(
    private val permissionManager: PermissionManager,
) {

    suspend operator fun invoke(permission: Permission): PermissionResult {
        return permissionManager.requestPermission(permission)
    }
}