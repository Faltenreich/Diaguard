package com.faltenreich.diaguard.shared.permission

class RequestPermissionIfNeededUseCase(
    private val permissionManager: PermissionManager,
) {

    suspend operator fun invoke(permission: Permission) {
        if (!permissionManager.hasPermission(permission)) {
            permissionManager.requestPermission(permission)
        }
    }
}