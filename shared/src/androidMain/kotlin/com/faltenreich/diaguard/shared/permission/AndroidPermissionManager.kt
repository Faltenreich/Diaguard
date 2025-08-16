package com.faltenreich.diaguard.shared.permission

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class AndroidPermissionManager(
    private val activity: Activity,
) : PermissionManager {

    override fun hasPermission(permission: Permission): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, permission.code)
        return result == PackageManager.PERMISSION_GRANTED
    }

    override fun requestPermission(permission: Permission) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(permission.code),
            REQUEST_CODE,
        )
    }

    companion object {

        private const val REQUEST_CODE = 1337
    }
}