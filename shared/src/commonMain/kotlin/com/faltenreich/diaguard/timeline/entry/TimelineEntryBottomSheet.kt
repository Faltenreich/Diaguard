package com.faltenreich.diaguard.timeline.entry

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.measurement.value.MeasurementValue

@Composable
fun TimelineEntryBottomSheet(
    values: List<MeasurementValue.Local>,
    modifier: Modifier = Modifier,
) {
    Text("Values: $values", modifier = modifier)
}