package com.faltenreich.diaguard.shared.permission

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import kotlin.coroutines.suspendCoroutine

class AndroidPermissionManager : PermissionManager {

    private lateinit var activity: ComponentActivity
    private lateinit var activityResultLauncher: ActivityResultLauncher<Array<String>>

    private var callback: ((PermissionResult) -> Unit)? = null

    fun bind(activity: ComponentActivity) {
        this.activity = activity
        this.activityResultLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { permissions ->
            permissions.forEach { (code, isGranted) ->
                val permission = Permission.fromCode(code)
                if (permission != null) {
                    val result = PermissionResult(permission, isGranted)
                    callback?.invoke(result)
                }
            }
        }
    }

    override suspend fun hasPermission(permission: Permission): Boolean {
        // TODO: Return early if below minSdk of permission
        val result = ContextCompat.checkSelfPermission(activity, permission.code)
        return result == PackageManager.PERMISSION_GRANTED
    }

    override suspend fun requestPermission(permission: Permission): PermissionResult {
        return suspendCoroutine { continuation ->
            callback = { result ->
                callback = null
                continuation.resumeWith(Result.success(result))
            }
            activityResultLauncher.launch(arrayOf(permission.code))
        }
    }
}