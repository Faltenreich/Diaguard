@file:JvmName("LegacyModuleCommon")

package com.faltenreich.diaguard.startup.legacy

import com.faltenreich.diaguard.data.legacy.LegacyRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import kotlin.jvm.JvmName

fun legacyModule() = module {
    factoryOf(::LegacyRepository)

    factoryOf(::ImportLegacyPreferencesUseCase)
    factoryOf(::ImportLegacyDatabaseUseCase)
    factoryOf(::ImportLegacyUseCase)
}