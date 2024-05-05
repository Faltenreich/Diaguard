package com.faltenreich.diaguard.measurement.unit.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormIntent
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.TextDivider
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.measurement_units

@Composable
fun MeasurementUnitList(
    items: List<MeasurementUnitListItemState>,
    onIntent: (MeasurementPropertyFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        TextDivider(getString(Res.string.measurement_units))
        items.forEach { item ->
            MeasurementUnitListItem(
                state = item,
                modifier = Modifier.clickable {
                    onIntent(MeasurementPropertyFormIntent.SelectUnit(item.unit))
                },
            )
        }
    }
}