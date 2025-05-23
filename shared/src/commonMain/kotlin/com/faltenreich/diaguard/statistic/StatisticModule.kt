package com.faltenreich.diaguard.statistic

import com.faltenreich.diaguard.statistic.average.GetAverageUseCase
import com.faltenreich.diaguard.statistic.distribution.GetDistributionUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun statisticModule() = module {
    factoryOf(::GetAverageUseCase)
    factoryOf(::GetDistributionUseCase)

    viewModelOf(::StatisticViewModel)
}