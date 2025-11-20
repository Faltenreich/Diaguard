package com.faltenreich.diaguard.permission

class RequestPermissionUseCase(
    private val permissionManager: PermissionManager,
) {

    suspend operator fun invoke(permission: Permission): PermissionResult {
        return permissionManager.requestPermission(permission)
    }
}