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
import com.faltenreich.diaguard.view.theme.AppTheme
import com.faltenreich.diaguard.shared.view.preview.AppPreview
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
            Text(stringResource(aggregationStyle.labelResource))
            Text(
                text = stringResource(aggregationStyle.descriptionResource),
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