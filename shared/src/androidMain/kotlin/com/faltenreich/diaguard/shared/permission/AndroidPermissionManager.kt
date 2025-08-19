package com.faltenreich.diaguard.shared.permission

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.faltenreich.diaguard.shared.logging.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class AndroidPermissionManager : PermissionManager {

    private lateinit var activity: ComponentActivity
    private lateinit var activityResultLauncher: ActivityResultLauncher<Array<String>>

    private val results = MutableSharedFlow<PermissionResult>()

    fun bind(activity: ComponentActivity) {
        this.activity = activity
        // LifecycleOwners must call register before they are STARTED
        activityResultLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { permissions ->
            Logger.debug("Permissions granted: $permissions")
            permissions.forEach { (code, isGranted) ->
                val permission = Permission.fromCode(code)
                if (permission != null) {
                    val result = PermissionResult(permission, isGranted)
                    results.tryEmit(result)
                }
            }
        }
    }

    override fun hasPermission(permission: Permission): Boolean {
        // TODO: Return early if below minSdk of permission
        val result = ContextCompat.checkSelfPermission(activity, permission.code)
        return result == PackageManager.PERMISSION_GRANTED
    }

    override fun requestPermission(permission: Permission): Flow<PermissionResult?> {
        activityResultLauncher.launch(arrayOf(permission.code))
        return results
    }
}