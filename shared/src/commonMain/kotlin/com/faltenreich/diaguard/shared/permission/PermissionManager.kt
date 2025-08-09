package com.faltenreich.diaguard.shared.permission

interface PermissionManager {

    fun hasPermission(permission: Permission): Boolean

    fun requestPermission(permission: Permission)
}