package com.faltenreich.diaguard.measurement.property.aggregationstyle

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.faltenreich.diaguard.data.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import com.faltenreich.diaguard.view.theme.AppTheme
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.aggregation_style_average
import diaguard.shared.generated.resources.aggregation_style_average_description
import diaguard.shared.generated.resources.aggregation_style_cumulative
import diaguard.shared.generated.resources.aggregation_style_cumulative_description
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MeasurementAggregationStyleListItem(
    aggregationStyle: MeasurementAggregationStyle,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .selectable(
                selected = isSelected,
                onClick = { onClick() },
                role = Role.RadioButton,
            )
            .padding(
                horizontal = AppTheme.dimensions.padding.P_3_5,
                vertical = AppTheme.dimensions.padding.P_3,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                stringResource(
                    when (aggregationStyle) {
                        MeasurementAggregationStyle.CUMULATIVE -> Res.string.aggregation_style_cumulative
                        MeasurementAggregationStyle.AVERAGE -> Res.string.aggregation_style_average
                    }
                )
            )
            Text(
                text = stringResource(
                    when (aggregationStyle) {
                        MeasurementAggregationStyle.CUMULATIVE -> Res.string.aggregation_style_cumulative_description
                        MeasurementAggregationStyle.AVERAGE -> Res.string.aggregation_style_average_description
                    }
                ),
                style = AppTheme.typography.bodySmall,
            )
        }
        RadioButton(
            selected = isSelected,
            onClick = null,
        )
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    MeasurementAggregationStyleListItem(
        aggregationStyle = MeasurementAggregationStyle.CUMULATIVE,
        isSelected = true,
        onClick = {},
    )
}