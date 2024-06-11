/**
 * Workaround for: Duplicate JVM class name
 * https://youtrack.jetbrains.com/issue/KT-21186
 */
@file:JvmName("LegacyModuleJvm")

package com.faltenreich.diaguard.backup.legacy

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import kotlin.jvm.JvmName

expect fun legacyDaoModule(): Module

fun legacyModule() = module {
    includes(legacyDaoModule())
    singleOf(::LegacyRepository)
    singleOf(::LegacyImport)
}