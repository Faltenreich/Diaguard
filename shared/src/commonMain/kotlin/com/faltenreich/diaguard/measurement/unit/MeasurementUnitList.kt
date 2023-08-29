package com.faltenreich.diaguard.measurement.unit

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MeasurementUnitList(
    units: List<MeasurementUnit>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(
            items = units,
            key = MeasurementUnit::id,
        ) { unit ->
            MeasurementUnitListItem(
                unit = unit,
                modifier = Modifier.animateItemPlacement(),
            )
        }
    }
}