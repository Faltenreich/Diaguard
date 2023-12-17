package com.faltenreich.diaguard.statistic

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun statisticModule() = module {
    singleOf(::GetAverageUseCase)
    singleOf(::StatisticViewModel)
}