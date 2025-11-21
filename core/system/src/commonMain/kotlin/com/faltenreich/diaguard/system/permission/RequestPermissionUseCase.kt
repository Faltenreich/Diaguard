package com.faltenreich.diaguard.system.permission

class RequestPermissionUseCase(
    private val permissionManager: PermissionManager,
) {

    suspend operator fun invoke(permission: Permission): PermissionResult {
        return permissionManager.requestPermission(permission)
    }
}