package com.faltenreich.diaguard.shared.config

interface BuildConfig {

    fun getVersionCode(): Long

    fun getVersionName(): String
}