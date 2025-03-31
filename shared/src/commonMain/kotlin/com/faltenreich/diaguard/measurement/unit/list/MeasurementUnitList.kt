package com.faltenreich.diaguard.measurement.unit.list

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit

@Composable
fun MeasurementUnitList(
    viewModel: MeasurementUnitListViewModel,
    modifier: Modifier = Modifier,
) {
    val units = viewModel.collectState()?.units ?: return
    LazyColumn(modifier = modifier) {
        items(units, key = MeasurementUnit.Local::id) { unit ->
            MeasurementUnitListItem(unit = unit)
        }
    }
}