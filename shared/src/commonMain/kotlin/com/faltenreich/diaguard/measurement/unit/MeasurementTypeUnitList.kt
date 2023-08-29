package com.faltenreich.diaguard.measurement.unit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.measurement.type.form.MeasurementTypeUnitListItem

@Composable
fun MeasurementTypeUnitList(
    typeUnits: List<MeasurementTypeUnit>,
    onItemClick: (MeasurementTypeUnit) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(
            items = typeUnits,
            key = MeasurementTypeUnit::id,
        ) { typeUnit ->
            MeasurementTypeUnitListItem(
                typeUnit = typeUnit,
                modifier = Modifier
                    .animateItemPlacement()
                    .clickable { onItemClick(typeUnit) },
            )
        }
    }
}