package com.faltenreich.diaguard.statistic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.view.Divider
import com.faltenreich.diaguard.view.TextDivider
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import com.faltenreich.diaguard.statistic.average.StatisticAverage
import com.faltenreich.diaguard.statistic.category.StatisticCategory
import com.faltenreich.diaguard.statistic.daterange.StatisticDateRangeBar
import com.faltenreich.diaguard.statistic.daterange.StatisticDateRangeButton
import com.faltenreich.diaguard.statistic.daterange.StatisticDateRangeState
import com.faltenreich.diaguard.statistic.daterange.StatisticDateRangeType
import com.faltenreich.diaguard.statistic.distribution.StatisticDistribution
import com.faltenreich.diaguard.statistic.property.StatisticProperty
import com.faltenreich.diaguard.statistic.trend.StatisticTrend
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.average
import diaguard.shared.generated.resources.distribution
import diaguard.shared.generated.resources.trend
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Statistic(
    state: StatisticState?,
    onIntent: (StatisticIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    state ?: return
    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
        ) {
            StatisticDateRangeButton(state.dateRange, onIntent)
            Divider()
            StatisticCategory(state.category, onIntent)
            StatisticProperty(state.property, onIntent)

            TextDivider(stringResource(Res.string.average))
            StatisticAverage(state.average)

            TextDivider(stringResource(Res.string.trend))
            StatisticTrend(state.trend)

            TextDivider(stringResource(Res.string.distribution))
            StatisticDistribution(state.distribution)
        }
        StatisticDateRangeBar(state.dateRange, onIntent)
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    Statistic(
        state = StatisticState(
            dateRange = StatisticDateRangeState(
                type = StatisticDateRangeType.WEEK,
                dateRange = today() .. today(),
                title = "title",
                subtitle = "subtitle",
            ),
        ),
        onIntent = {},
    )
}