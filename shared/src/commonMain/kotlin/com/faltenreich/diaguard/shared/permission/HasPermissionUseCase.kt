package com.faltenreich.diaguard.shared.permission

class HasPermissionUseCase(
    private val permissionManager: PermissionManager,
) {

    suspend operator fun invoke(permission: Permission): Boolean {
        return permissionManager.hasPermission(permission)
    }
}