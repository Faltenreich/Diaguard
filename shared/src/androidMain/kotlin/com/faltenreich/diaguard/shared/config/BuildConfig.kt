package com.faltenreich.diaguard.shared.config

import android.content.Context
import android.content.pm.PackageInfo
import androidx.core.content.pm.PackageInfoCompat

actual class BuildConfig(private val context: Context) {

    private fun getPackageInfo(): PackageInfo {
        @Suppress("DEPRECATION")
        return context.packageManager.getPackageInfo(context.packageName, 0)
    }

    actual fun getBuildNumber(): Long {
        return PackageInfoCompat.getLongVersionCode(getPackageInfo())
    }

    actual fun getVersionName(): String {
        return getPackageInfo().versionName
    }
}