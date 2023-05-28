package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.log.usecase.GetLogItemsUseCase
import com.faltenreich.diaguard.log.usecase.MapLogItemsUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun logModule() = module {
    singleOf(::LogViewModel)
    singleOf(::GetLogItemsUseCase)
    singleOf(::MapLogItemsUseCase)
}