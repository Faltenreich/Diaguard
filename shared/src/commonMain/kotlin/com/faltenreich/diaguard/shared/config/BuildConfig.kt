package com.faltenreich.diaguard.shared.config

expect class BuildConfig {

    fun getBuildNumber(): Long
    fun getVersionName(): String
}