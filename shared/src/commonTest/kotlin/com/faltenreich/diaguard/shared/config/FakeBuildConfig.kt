package com.faltenreich.diaguard.shared.config

class FakeBuildConfig : BuildConfig {

    override fun getBuildNumber(): Long = 1

    override fun getVersionName(): String = "1.0.0"
}