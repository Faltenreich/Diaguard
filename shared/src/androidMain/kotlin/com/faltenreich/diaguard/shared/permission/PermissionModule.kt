package com.faltenreich.diaguard.shared.permission

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun permissionModule() = module {
    factoryOf(::AndroidPermissionManager) bind PermissionManager::class
}