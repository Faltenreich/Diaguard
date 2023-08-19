package com.faltenreich.diaguard.measurement.property.list

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun MeasurementPropertyList(
    modifier: Modifier = Modifier,
    viewModel: MeasurementPropertyListViewModel = inject(),
) {
    val state = viewModel.viewState.collectAsState().value
    LazyColumn(modifier = modifier) {
        items(state, key = MeasurementProperty::id) { property ->
            MeasurementPropertyListItem(property)
        }
    }
}