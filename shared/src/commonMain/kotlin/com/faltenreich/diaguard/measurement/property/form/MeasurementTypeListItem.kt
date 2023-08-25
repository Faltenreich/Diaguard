package com.faltenreich.diaguard.measurement.property.form

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.ResourceIcon

@Composable
fun MeasurementTypeListItem(
    type: MeasurementType,
    onArrowUp: ((MeasurementType) -> Unit)?,
    onArrowDown: ((MeasurementType) -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        FormRow(icon = { /* TODO */ }) {
            Column(modifier = Modifier.weight(1f)) {
                Text(type.name)
                Text(
                    text = type.selectedUnit?.name ?: "",
                    style = AppTheme.typography.bodySmall,
                )
            }
            IconButton(
                onClick = { onArrowUp?.invoke(type) },
                modifier = Modifier.alpha(if (onArrowUp != null) 1f else 0f),
            ) {
                ResourceIcon(MR.images.ic_arrow_up)
            }
            IconButton(
                onClick = { onArrowDown?.invoke(type) },
                modifier = Modifier.alpha(if (onArrowDown != null) 1f else 0f),
            ) {
                ResourceIcon(MR.images.ic_arrow_down)
            }
        }
        Divider()
    }
}