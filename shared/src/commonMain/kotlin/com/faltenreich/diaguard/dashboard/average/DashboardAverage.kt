package com.faltenreich.diaguard.dashboard.average

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.view.theme.AppTheme
import com.faltenreich.diaguard.data.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.average
import diaguard.shared.generated.resources.day
import diaguard.shared.generated.resources.month
import diaguard.shared.generated.resources.placeholder
import diaguard.shared.generated.resources.week
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DashboardAverage(
    state: DashboardAverageState?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier,
    ) {
        Box(modifier = Modifier.padding(all = AppTheme.dimensions.padding.P_3)) {
            Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3)) {
                Text(
                    text = stringResource(Res.string.average),
                    style = AppTheme.typography.labelMedium,
                )
                Row {
                    Text(
                        text = stringResource(Res.string.day),
                        modifier = Modifier.weight(1f),
                    )
                    Text(state?.day?.value ?: stringResource(Res.string.placeholder))
                }
                Row {
                    Text(
                        text = stringResource(Res.string.week),
                        modifier = Modifier.weight(1f),
                    )
                    Text(state?.week?.value ?: stringResource(Res.string.placeholder))
                }
                Row {
                    Text(
                        text = stringResource(Res.string.month),
                        modifier = Modifier.weight(1f),
                    )
                    Text(state?.month?.value ?: stringResource(Res.string.placeholder))
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewLatest() = AppPreview {
    DashboardAverage(
        state = DashboardAverageState(
            day = MeasurementValue.Localized(
                value = "day",
            ),
            week = MeasurementValue.Localized(
                value = "week",
            ),
            month = MeasurementValue.Localized(
                value = "month"
            ),
        ),
        onClick = {},
    )
}