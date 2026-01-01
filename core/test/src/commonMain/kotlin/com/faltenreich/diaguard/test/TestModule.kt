package com.faltenreich.diaguard.test

import com.faltenreich.diaguard.config.BuildConfig
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal fun testModule() = module {
    factoryOf(::TestBuildConfig) bind BuildConfig::class
}