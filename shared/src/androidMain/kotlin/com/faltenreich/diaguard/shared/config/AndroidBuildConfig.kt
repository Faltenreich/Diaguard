package com.faltenreich.diaguard.shared.config

import android.content.Context
import android.content.pm.PackageInfo
import androidx.core.content.pm.PackageInfoCompat

class AndroidBuildConfig(private val context: Context) : BuildConfig {

    private fun getPackageInfo(): PackageInfo {
        @Suppress("DEPRECATION")
        return context.packageManager.getPackageInfo(context.packageName, 0)
    }

    override fun getVersionCode(): Long {
        return PackageInfoCompat.getLongVersionCode(getPackageInfo())
    }

    override fun getVersionName(): String {
        return getPackageInfo().versionName
    }
}