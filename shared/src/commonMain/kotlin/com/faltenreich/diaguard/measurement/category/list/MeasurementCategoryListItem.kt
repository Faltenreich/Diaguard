package com.faltenreich.diaguard.measurement.category.list

import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.icon.MeasurementCategoryIcon
import com.faltenreich.diaguard.view.FormRow
import com.faltenreich.diaguard.view.ResourceIcon
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_arrow_down
import diaguard.shared.generated.resources.ic_arrow_up
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MeasurementCategoryListItem(
    category: MeasurementCategory.Local,
    onIntent: (MeasurementCategoryListIntent) -> Unit,
    showArrowUp: Boolean,
    showArrowDown: Boolean,
    modifier: Modifier = Modifier,
) {
    FormRow(
        modifier = modifier,
        icon = { MeasurementCategoryIcon(category) },
    ) {
        Text(
            text = category.name,
            modifier = Modifier.weight(1f),
            style = LocalTextStyle.current.copy(
                textDecoration =
                    if (category.isActive) TextDecoration.None
                    else TextDecoration.LineThrough,
            ),
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
}

@Preview
@Composable
private fun Preview() = AppPreview {
    MeasurementCategoryListItem(
        category = category(),
        onIntent = {},
        showArrowUp = true,
        showArrowDown = true,
    )
}