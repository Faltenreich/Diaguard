package com.faltenreich.diaguard.measurement.property.aggregationstyle

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.color_scheme

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
            text = getString(Res.string.color_scheme),
            style = AppTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_2))

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