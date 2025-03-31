package com.faltenreich.diaguard.measurement.unit.list

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit

@Composable
fun MeasurementUnitListItem(
    unit: MeasurementUnit.Local,
    modifier: Modifier = Modifier,
) {
    Text(unit.name, modifier =  modifier)
}