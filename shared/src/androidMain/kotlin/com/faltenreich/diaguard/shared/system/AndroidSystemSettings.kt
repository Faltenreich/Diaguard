package com.faltenreich.diaguard.shared.system

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings

class AndroidSystemSettings(private val context: Context) : SystemSettings {

    private fun getIntentForAppSettings(): Intent {
        return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .setData(Uri.fromParts("package", context.packageName, null))
    }

    override fun openNotificationSettings() {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        } else {
            getIntentForAppSettings()
        }
        context.startActivity(intent)
    }

    override fun openPermissionSettings() {
        context.startActivity(getIntentForAppSettings())
    }
}