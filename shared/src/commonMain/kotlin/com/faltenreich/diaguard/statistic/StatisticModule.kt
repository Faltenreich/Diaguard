package com.faltenreich.diaguard.statistic

import com.faltenreich.diaguard.statistic.average.GetAverageUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun statisticModule() = module {
    factoryOf(::GetAverageUseCase)

    viewModelOf(::StatisticViewModel)
}