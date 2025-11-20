package com.faltenreich.diaguard.permission

class HasPermissionUseCase(
    private val permissionManager: PermissionManager,
) {

    suspend operator fun invoke(permission: Permission): Boolean {
        return permissionManager.hasPermission(permission)
    }
}