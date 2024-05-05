package com.faltenreich.diaguard.measurement.category.list

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.icon.MeasurementCategoryIcon
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.ResourceIcon
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_arrow_down
import diaguard.shared.generated.resources.ic_arrow_up

@Composable
fun MeasurementCategoryListItem(
    category: MeasurementCategory,
    onArrowUp: () -> Unit,
    showArrowUp: Boolean,
    onArrowDown: () -> Unit,
    showArrowDown: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        FormRow(icon = { MeasurementCategoryIcon(category) }) {
            Text(
                text = category.name,
                modifier = Modifier.weight(1f),
            )
            IconButton(
                onClick = { onArrowUp() },
                modifier = Modifier.alpha(if (showArrowUp) 1f else 0f),
            ) {
                ResourceIcon(Res.drawable.ic_arrow_up)
            }
            IconButton(
                onClick = { onArrowDown() },
                modifier = Modifier.alpha(if (showArrowDown) 1f else 0f),
            ) {
                ResourceIcon(Res.drawable.ic_arrow_down)
            }
        }
        Divider()
    }
}