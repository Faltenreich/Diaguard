package com.faltenreich.diaguard.measurement.property.form

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.ResourceIcon

@Composable
fun MeasurementTypeListItem(
    type: MeasurementType,
    onArrowUp: (MeasurementType) -> Unit,
    showArrowUp: Boolean,
    onArrowDown: (MeasurementType) -> Unit,
    showArrowDown: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        FormRow {
            Text(
                text = type.name,
                modifier = Modifier.weight(1f),
            )
            IconButton(
                onClick = { onArrowUp(type) },
                modifier = Modifier.alpha(if (showArrowUp) 1f else 0f),
            ) {
                ResourceIcon(MR.images.ic_arrow_up)
            }
            IconButton(
                onClick = { onArrowDown(type) },
                modifier = Modifier.alpha(if (showArrowDown) 1f else 0f),
            ) {
                ResourceIcon(MR.images.ic_arrow_down)
            }
        }
        Divider()
    }
}