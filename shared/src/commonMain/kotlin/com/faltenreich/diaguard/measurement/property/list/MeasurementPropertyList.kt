package com.faltenreich.diaguard.measurement.property.list

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun MeasurementPropertyList(
    modifier: Modifier = Modifier,
    viewModel: MeasurementPropertyListViewModel = inject(),
) {
    when (val state = viewModel.viewState.collectAsState().value) {
        is MeasurementPropertyListViewState.Loading -> Unit
        is MeasurementPropertyListViewState.Loaded -> LazyColumn(modifier = modifier) {
            val properties = state.listItems
            itemsIndexed(
                items = properties,
                key = { _, property -> property.id },
            ) { index, property ->
                MeasurementPropertyListItem(
                    property = property,
                    onArrowUp = viewModel::decrementSortIndex.takeIf { index > 0 },
                    onArrowDown = viewModel::incrementSortIndex.takeIf { index < properties.size - 1 },
                    modifier = Modifier.animateItemPlacement(),
                )
            }
        }
    }
}