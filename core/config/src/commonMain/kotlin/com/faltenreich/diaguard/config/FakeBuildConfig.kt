package com.faltenreich.diaguard.config

class FakeBuildConfig : BuildConfig {

    override fun getPackageName(): String = "com.faltenreich.diaguard"

    override fun getVersionCode(): Long = 1

    override fun getVersionName(): String = "1.0.0"
}