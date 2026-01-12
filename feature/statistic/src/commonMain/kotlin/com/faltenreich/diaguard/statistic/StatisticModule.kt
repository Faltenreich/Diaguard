package com.faltenreich.diaguard.statistic

import com.faltenreich.diaguard.data.dataModule
import com.faltenreich.diaguard.measurement.measurementModule
import com.faltenreich.diaguard.navigation.navigationModule
import com.faltenreich.diaguard.preference.preferenceModule
import com.faltenreich.diaguard.statistic.average.GetStatisticAverageUseCase
import com.faltenreich.diaguard.statistic.distribution.GetStatisticDistributionUseCase
import com.faltenreich.diaguard.statistic.trend.GetStatisticTrendUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun statisticModule() = module {
    includes(
        dataModule(),
        measurementModule(),
        navigationModule(),
        preferenceModule(),
    )

    factoryOf(::GetStatisticAverageUseCase)
    factoryOf(::GetStatisticTrendUseCase)
    factoryOf(::GetStatisticDistributionUseCase)

    viewModelOf(::StatisticViewModel)
}