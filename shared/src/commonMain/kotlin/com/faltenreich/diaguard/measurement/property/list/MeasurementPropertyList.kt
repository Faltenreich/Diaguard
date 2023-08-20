package com.faltenreich.diaguard.measurement.property.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun MeasurementPropertyList(
    modifier: Modifier = Modifier,
    viewModel: MeasurementPropertyListViewModel = inject(),
) {
    val navigator = LocalNavigator.currentOrThrow
    when (val state = viewModel.viewState.collectAsState().value) {
        is MeasurementPropertyListViewState.Loading -> Unit
        is MeasurementPropertyListViewState.Loaded -> LazyColumn(modifier = modifier) {
            val listItems = state.listItems
            itemsIndexed(
                items = listItems,
                key = { _, item -> item.id },
            ) { index, item ->
                MeasurementPropertyListItem(
                    property = item,
                    onArrowUp = viewModel::decrementSortIndex.takeIf { index > 0 },
                    onArrowDown = viewModel::incrementSortIndex.takeIf { index < listItems.size - 1 },
                    modifier = Modifier
                        .animateItemPlacement()
                        .clickable { navigator.push(Screen.MeasurementPropertyForm(item)) },
                )
            }
        }
    }
}