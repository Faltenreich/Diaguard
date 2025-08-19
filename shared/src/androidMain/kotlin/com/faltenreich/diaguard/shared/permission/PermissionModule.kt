package com.faltenreich.diaguard.shared.permission

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun permissionModule() = module {
    singleOf(::AndroidPermissionManager) bind PermissionManager::class

    // TODO: Move into common
    factoryOf(::RequestPermissionUseCase)
}