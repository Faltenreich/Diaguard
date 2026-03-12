package com.faltenreich.diaguard.system.permission

class RequestPermissionUseCase(
    private val permissionManager: PermissionManager,
): PermissionManager by permissionManager {

    suspend operator fun invoke(permission: Permission): PermissionResult {
        return permissionManager.requestPermission(permission)
    }
}