@file:JvmName("LegacyModuleCommon")

package com.faltenreich.diaguard.startup.legacy

import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import kotlin.jvm.JvmName

expect fun legacyDaoModule(): Module

fun legacyModule() = module {
    includes(legacyDaoModule())

    factoryOf(::LegacyRepository)

    factoryOf(::ImportLegacyPreferencesUseCase)
    factoryOf(::ImportLegacyDatabaseUseCase)
    factoryOf(::ImportLegacyUseCase)
}