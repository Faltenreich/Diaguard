package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.log.usecase.GetLogDataUseCase
import com.faltenreich.diaguard.log.usecase.MapLogDataUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun logModule() = module {
    singleOf(::LogViewModel)
    singleOf(::GetLogDataUseCase)
    singleOf(::MapLogDataUseCase)
}