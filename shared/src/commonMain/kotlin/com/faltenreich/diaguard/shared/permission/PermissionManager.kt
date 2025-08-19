package com.faltenreich.diaguard.shared.permission

import kotlinx.coroutines.flow.Flow

interface PermissionManager {

    fun hasPermission(permission: Permission): Boolean

    fun requestPermission(permission: Permission): Flow<PermissionResult?>
}