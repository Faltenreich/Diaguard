package com.faltenreich.diaguard.shared.config

interface BuildConfig {

    fun getBuildNumber(): Long

    fun getVersionName(): String
}