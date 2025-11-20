package com.faltenreich.diaguard.permission

interface PermissionManager {

    suspend fun hasPermission(permission: Permission): Boolean

    suspend fun shouldShowRequestPermissionRationale(permission: Permission): Boolean

    suspend fun requestPermission(permission: Permission): PermissionResult
}