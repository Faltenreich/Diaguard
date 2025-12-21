package com.faltenreich.diaguard.test

import com.faltenreich.diaguard.config.BuildConfig

class TestBuildConfig : BuildConfig {

    override fun getPackageName(): String = "com.faltenreich.diaguard"

    override fun getVersionCode(): Long = 1

    override fun getVersionName(): String = "1.0.0"

    override fun isPersistent(): Boolean = false
}