package com.faltenreich.diaguard.measurement.unit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MeasurementUnitList(
    units: List<MeasurementUnit>,
    onItemClick: (MeasurementUnit) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(
            items = units,
            key = MeasurementUnit::id,
        ) { unit ->
            MeasurementUnitListItem(
                unit = unit,
                modifier = Modifier
                    .animateItemPlacement()
                    .clickable { onItemClick(unit) },
            )
        }
    }
}