package com.faltenreich.diaguard.shared.config

interface BuildConfig {

    fun getPackageName(): String

    fun getVersionCode(): Long

    fun getVersionName(): String
}