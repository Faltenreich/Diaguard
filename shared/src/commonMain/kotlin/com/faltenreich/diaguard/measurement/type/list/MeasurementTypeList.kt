package com.faltenreich.diaguard.measurement.type.list

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun MeasurementTypeList(
    modifier: Modifier = Modifier,
    viewModel: MeasurementTypeListViewModel = inject(),
) {
    when (val state = viewModel.viewState.collectAsState().value) {
        is MeasurementTypeListViewState.Loading -> Unit
        is MeasurementTypeListViewState.Loaded -> LazyColumn(modifier = modifier) {
            val listItems = state.listItems
            itemsIndexed(
                items = listItems,
                key = { _, item -> item.id },
            ) { index, item ->
                MeasurementTypeListItem(
                    type = item,
                    onArrowUp = viewModel::decrementSortIndex.takeIf { index > 0 },
                    onArrowDown = viewModel::incrementSortIndex.takeIf { index < listItems.size - 1 },
                    modifier = Modifier.animateItemPlacement(),
                )
            }
        }
    }
}