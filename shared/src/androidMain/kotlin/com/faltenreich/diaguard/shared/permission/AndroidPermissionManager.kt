package com.faltenreich.diaguard.shared.permission

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.suspendCoroutine

class AndroidPermissionManager : PermissionManager {

    private lateinit var activity: ComponentActivity
    private lateinit var activityResultLauncher: ActivityResultLauncher<Array<String>>

    private val mutex: Mutex = Mutex()
    private var callback: ((PermissionResult) -> Unit)? = null

    fun bind(activity: ComponentActivity) {
        this.activity = activity
        this.activityResultLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { permissions ->
            permissions.forEach { (code, isGranted) ->
                val permission = Permission.fromCode(code)
                if (permission != null) {
                    callback?.invoke(PermissionResult(permission, isGranted))
                }
                // TODO: Error handling
            }
        }
    }

    override suspend fun requestPermission(permission: Permission): PermissionResult {
        return mutex.withLock {
            val code = permission.code
            if (code == null) {
                PermissionResult(permission, isGranted = true)
            } else if (hasPermission(code)) {
                PermissionResult(permission, isGranted = true)
            } else {
                suspendCoroutine { continuation ->
                    callback = { result ->
                        callback = null
                        continuation.resumeWith(Result.success(result))
                    }
                    activityResultLauncher.launch(arrayOf(code))
                }
            }
        }
    }

    private fun hasPermission(code: String): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, code)
        return result == PackageManager.PERMISSION_GRANTED
    }
}