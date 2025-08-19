package com.faltenreich.diaguard.shared.permission

interface PermissionManager {

    suspend fun requestPermission(permission: Permission): PermissionResult
}