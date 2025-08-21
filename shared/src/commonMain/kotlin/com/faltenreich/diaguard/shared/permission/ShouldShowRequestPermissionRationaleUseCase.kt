package com.faltenreich.diaguard.shared.permission

class ShouldShowRequestPermissionRationaleUseCase(
    private val permissionManager: PermissionManager,
) {

    suspend operator fun invoke(permission: Permission): Boolean {
        return permissionManager.shouldShowRequestPermissionRationale(permission)
    }
}