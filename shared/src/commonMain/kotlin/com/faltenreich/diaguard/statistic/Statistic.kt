package com.faltenreich.diaguard.statistic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.faltenreich.diaguard.datetime.picker.DateRangePickerDialog
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.ResourceIcon
import com.faltenreich.diaguard.shared.view.TextDivider
import com.faltenreich.diaguard.statistic.average.StatisticAverage
import com.faltenreich.diaguard.statistic.category.StatisticCategory
import com.faltenreich.diaguard.statistic.distribution.StatisticDistribution
import com.faltenreich.diaguard.statistic.property.StatisticProperty
import com.faltenreich.diaguard.statistic.trend.StatisticTrend
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.average
import diaguard.shared.generated.resources.date_range_picker_open
import diaguard.shared.generated.resources.distribution
import diaguard.shared.generated.resources.ic_time
import diaguard.shared.generated.resources.trend
import org.jetbrains.compose.resources.stringResource

@Composable
fun Statistic(
    viewModel: StatisticViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.collectState() ?: return
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        DateRange(state, onIntent = viewModel::dispatchIntent)

        state.category?.let { category ->
            Divider()
            StatisticCategory(category, onIntent = viewModel::dispatchIntent)
        }

        state.property?.let { property ->
            Divider()
            StatisticProperty(property, onIntent = viewModel::dispatchIntent)
        }

        state.average?.let { average ->
            TextDivider(getString(Res.string.average))
            StatisticAverage(average)
        }

        state.trend?.let { trend ->
            TextDivider(getString(Res.string.trend))
            StatisticTrend(trend)
        }

        state.distribution?.let { distribution ->
            TextDivider(getString(Res.string.distribution))
            StatisticDistribution(distribution)
        }
    }
}

@Composable
private fun DateRange(
    state: StatisticState,
    onIntent: (StatisticIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDateRangePicker by remember { mutableStateOf(false) }

    FormRow(
        icon = { ResourceIcon(Res.drawable.ic_time) },
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClickLabel = stringResource(Res.string.date_range_picker_open),
                role = Role.Button,
                onClick = { showDateRangePicker = true },
            ),
    ) {
        Text(state.dateRangeLocalized)
    }

    if (showDateRangePicker) {
        DateRangePickerDialog(
            dateRange = state.dateRange,
            onDismissRequest = { showDateRangePicker = false },
            onConfirmRequest = { dateRange ->
                showDateRangePicker = false
                onIntent(StatisticIntent.SetDateRange(dateRange))
            },
        )
    }
}