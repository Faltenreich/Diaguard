@file:JvmName("PlatformPermissionModule")

package com.faltenreich.diaguard.permission

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun platformPermissionModule() = module {
    singleOf(::AndroidPermissionManager) bind PermissionManager::class
}