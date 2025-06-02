package com.faltenreich.diaguard.statistic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.TextDivider
import com.faltenreich.diaguard.statistic.average.StatisticAverage
import com.faltenreich.diaguard.statistic.category.StatisticCategory
import com.faltenreich.diaguard.statistic.daterange.StatisticDateRange
import com.faltenreich.diaguard.statistic.daterange.StatisticDateRangeBar
import com.faltenreich.diaguard.statistic.distribution.StatisticDistribution
import com.faltenreich.diaguard.statistic.property.StatisticProperty
import com.faltenreich.diaguard.statistic.trend.StatisticTrend
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.average
import diaguard.shared.generated.resources.distribution
import diaguard.shared.generated.resources.trend

@Composable
fun Statistic(
    viewModel: StatisticViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.collectState() ?: return
    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
        ) {
            StatisticDateRange(state.dateRange, onIntent = viewModel::dispatchIntent)
            Divider()
            StatisticCategory(state.category, onIntent = viewModel::dispatchIntent)
            StatisticProperty(state.property, onIntent = viewModel::dispatchIntent)

            TextDivider(getString(Res.string.average))
            StatisticAverage(state.average)

            TextDivider(getString(Res.string.trend))
            StatisticTrend(state.trend)

            TextDivider(getString(Res.string.distribution))
            StatisticDistribution(state.distribution)
        }
        Divider()
        StatisticDateRangeBar(
            state = state.dateRange,
            onIntent = viewModel::dispatchIntent,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}