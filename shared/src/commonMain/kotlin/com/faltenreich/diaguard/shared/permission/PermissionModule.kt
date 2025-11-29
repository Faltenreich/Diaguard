package com.faltenreich.diaguard.shared.permission

import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun permissionModule() = module {
    includes(platformPermissionModule())

    factoryOf(::HasPermissionUseCase)
    factoryOf(::RequestPermissionUseCase)
}

expect fun platformPermissionModule(): Module