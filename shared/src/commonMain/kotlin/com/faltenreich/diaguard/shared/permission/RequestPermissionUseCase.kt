package com.faltenreich.diaguard.shared.permission

class RequestPermissionUseCase(
    private val permissionManager: PermissionManager,
) {

    suspend operator fun invoke(permission: Permission): PermissionResult {
        return if (permissionManager.hasPermission(permission)) {
            return PermissionResult(permission, isGranted = true)
        } else {
            permissionManager.requestPermission(permission)
        }
    }
}