package com.faltenreich.diaguard.statistic

import com.faltenreich.diaguard.statistic.average.GetStatisticAverageUseCase
import com.faltenreich.diaguard.statistic.distribution.GetStatisticDistributionUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun statisticModule() = module {
    factoryOf(::GetStatisticAverageUseCase)
    factoryOf(::GetStatisticDistributionUseCase)

    viewModelOf(::StatisticViewModel)
}