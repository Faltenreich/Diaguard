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
    when (val state = viewModel.viewState.collectAsState().value) {
        is MeasurementPropertyListViewState.Loading -> Unit
        is MeasurementPropertyListViewState.Loaded -> LazyColumn(modifier = modifier) {
            items(state.listItems, key = MeasurementProperty::id) { property ->
                MeasurementPropertyListItem(property)
            }
        }
    }
}