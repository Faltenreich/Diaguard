package com.faltenreich.diaguard.measurement.property.aggregationstyle

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.data.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.aggregation_style
import com.faltenreich.diaguard.resource.aggregation_style_description
import com.faltenreich.diaguard.view.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MeasurementAggregationStyleForm(
    selection: MeasurementAggregationStyle,
    onChange: (MeasurementAggregationStyle) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(Res.string.aggregation_style),
            style = AppTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_3))

        Text(
            text = stringResource(Res.string.aggregation_style_description),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimensions.padding.P_3_5),
        )

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_3))

        Column(modifier = Modifier.selectableGroup()) {
            MeasurementAggregationStyle.entries.forEach { aggregationStyle ->
                MeasurementAggregationStyleListItem(
                    aggregationStyle = aggregationStyle,
                    isSelected = selection == aggregationStyle,
                    onClick = { onChange(aggregationStyle) },
                )
            }
        }

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_3))
    }
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    MeasurementAggregationStyleForm(
        selection = MeasurementAggregationStyle.CUMULATIVE,
        onChange = {},
    )
}