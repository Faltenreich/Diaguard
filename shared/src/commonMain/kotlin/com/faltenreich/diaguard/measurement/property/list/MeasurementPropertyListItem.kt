package com.faltenreich.diaguard.measurement.property.list

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.view.theme.AppTheme
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.view.FormRow
import com.faltenreich.diaguard.view.ResourceIcon
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_arrow_down
import diaguard.shared.generated.resources.ic_arrow_up
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MeasurementPropertyListItem(
    property: MeasurementProperty.Local,
    onArrowUp: (MeasurementProperty) -> Unit,
    showArrowUp: Boolean,
    onArrowDown: (MeasurementProperty) -> Unit,
    showArrowDown: Boolean,
    modifier: Modifier = Modifier,
) {
    FormRow(modifier = modifier) {
        Column(modifier = Modifier.weight(1f)) {
            Text(property.name)
            Text(
                text = property.unit.name,
                style = AppTheme.typography.bodySmall,
            )
        }
        IconButton(
            onClick = { onArrowUp(property) },
            enabled = showArrowUp,
        ) {
            ResourceIcon(Res.drawable.ic_arrow_up)
        }
        IconButton(
            onClick = { onArrowDown(property) },
            enabled = showArrowDown,
        ) {
            ResourceIcon(Res.drawable.ic_arrow_down)
        }
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    MeasurementPropertyListItem(
        property = property(),
        onArrowUp = {},
        showArrowUp = true,
        onArrowDown = {},
        showArrowDown = true,
    )
}