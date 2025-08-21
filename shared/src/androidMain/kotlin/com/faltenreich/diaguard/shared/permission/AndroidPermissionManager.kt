package com.faltenreich.diaguard.shared.permission

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
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
            val result = permissions.mapNotNull { (code, isGranted) ->
                Permission.fromCode(code)?.let { permission ->
                    if (isGranted) PermissionResult.Granted
                    else PermissionResult.Denied
                }
            }.firstOrNull()
            when (result) {
                null -> callback?.invoke(PermissionResult.Unknown)
                else -> callback?.invoke(result)
            }
        }
    }

    override suspend fun hasPermission(permission: Permission): Boolean {
        val code = permission.code ?: return true
        val result = ContextCompat.checkSelfPermission(activity, code)
        return result == PackageManager.PERMISSION_GRANTED
    }

    override suspend fun shouldShowRequestPermissionRationale(permission: Permission): Boolean {
        val code = permission.code ?: return false
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, code)
    }

    override suspend fun requestPermission(permission: Permission): PermissionResult {
        val code = permission.code ?: return PermissionResult.Granted
        return mutex.withLock {
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