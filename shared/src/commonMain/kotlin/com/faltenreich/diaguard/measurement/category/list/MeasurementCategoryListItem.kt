package com.faltenreich.diaguard.measurement.category.list

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Checkbox
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    onIntent: (MeasurementCategoryListIntent) -> Unit,
    showArrowUp: Boolean,
    showArrowDown: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        FormRow(icon = { MeasurementCategoryIcon(category) }) {
            Text(
                text = category.name,
                modifier = Modifier.weight(1f),
            )
            Checkbox(
                checked = category.isActive,
                onCheckedChange = { onIntent(MeasurementCategoryListIntent.ChangeIsActive(category)) },
            )
            IconButton(
                onClick = { onIntent(MeasurementCategoryListIntent.DecrementSortIndex(category)) },
                enabled = showArrowUp,
            ) {
                ResourceIcon(Res.drawable.ic_arrow_up)
            }
            IconButton(
                onClick = { onIntent(MeasurementCategoryListIntent.IncrementSortIndex(category)) },
                enabled = showArrowDown,
            ) {
                ResourceIcon(Res.drawable.ic_arrow_down)
            }
        }
        Divider()
    }
}