package com.faltenreich.diaguard.config

interface BuildConfig {

    fun getPackageName(): String

    fun getVersionCode(): Long

    fun getVersionName(): String
}