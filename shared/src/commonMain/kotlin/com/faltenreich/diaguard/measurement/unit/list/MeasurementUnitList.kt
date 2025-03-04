package com.faltenreich.diaguard.measurement.unit.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormIntent
import com.faltenreich.diaguard.shared.view.Divider

@Composable
fun MeasurementUnitList(
    items: List<MeasurementUnitListItemState>,
    onIntent: (MeasurementPropertyFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        items.forEachIndexed { index, item ->
            if (index != 0) {
                Divider()
            }
            MeasurementUnitListItem(
                state = item,
                modifier = Modifier.clickable {
                    onIntent(MeasurementPropertyFormIntent.SelectUnit(item.unit))
                },
            )
        }
    }
}