package com.faltenreich.diaguard.measurement.unit.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.FormRow
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_check
import diaguard.shared.generated.resources.measurement_unit_selected_description
import org.jetbrains.compose.resources.painterResource

@Composable
fun MeasurementUnitListItem(
    state: MeasurementUnitListItemState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        FormRow {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(state.title)
                    state.subtitle?.let { subtitle ->
                        Text(
                            text = subtitle,
                            style = AppTheme.typography.bodySmall,
                        )
                    }
                }
                AnimatedVisibility(visible = state.isSelected) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_check),
                        contentDescription = getString(Res.string.measurement_unit_selected_description),
                        modifier = modifier.size(AppTheme.dimensions.size.ImageMedium),
                        tint = AppTheme.colors.scheme.primary,
                    )
                }
            }
        }
        Divider()
    }
}