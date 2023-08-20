package com.faltenreich.diaguard.measurement.property.list

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyIcon
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.ResourceIcon

@Composable
fun MeasurementPropertyListItem(
    property: MeasurementProperty,
    onArrowUp: ((MeasurementProperty) -> Unit)?,
    onArrowDown: ((MeasurementProperty) -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        FormRow(icon = { MeasurementPropertyIcon(property) }) {
            Text(
                text = property.name,
                modifier = Modifier.weight(1f),
            )
            IconButton(
                onClick = { onArrowUp?.invoke(property) },
                modifier = Modifier.alpha(if (onArrowUp != null) 1f else 0f),
            ) {
                ResourceIcon(MR.images.ic_arrow_up)
            }
            IconButton(
                onClick = { onArrowDown?.invoke(property) },
                modifier = Modifier.alpha(if (onArrowDown != null) 1f else 0f),
            ) {
                ResourceIcon(MR.images.ic_arrow_down)
            }
        }
        Divider()
    }
}