package com.faltenreich.diaguard.shared.validation

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun validationModule() = module {
    singleOf(::ValidateUseCase)
}