package com.faltenreich.diaguard.measurement.property

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun MeasurementPropertyList(
    modifier: Modifier = Modifier,
    viewModel: MeasurementPropertyListViewModel = inject(),
) {
    Text("MeasurementPropertyList")
}