package com.faltenreich.diaguard.measurement.property.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormDialog
import com.faltenreich.diaguard.navigation.screen.MeasurementPropertyFormScreen
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.LoadingIndicator

@Composable
fun MeasurementPropertyList(
    modifier: Modifier = Modifier,
    viewModel: MeasurementPropertyListViewModel = inject(),
) {
    val navigator = LocalNavigator.currentOrThrow
    val state = viewModel.viewState.collectAsState().value
    when (state) {
        is MeasurementPropertyListViewState.Loading -> LoadingIndicator()
        is MeasurementPropertyListViewState.Loaded -> LazyColumn(modifier = modifier) {
            val listItems = state.listItems
            itemsIndexed(
                items = listItems,
                key = { _, item -> item.id },
            ) { index, item ->
                MeasurementPropertyListItem(
                    property = item,
                    onArrowUp = viewModel::decrementSortIndex,
                    showArrowUp = index > 0,
                    onArrowDown = viewModel::incrementSortIndex,
                    showArrowDown = index < listItems.size - 1,
                    modifier = Modifier
                        .animateItemPlacement()
                        .clickable { navigator.push(MeasurementPropertyFormScreen(item)) },
                )
            }
        }
    }
    if (state.showFormDialog) {
        MeasurementPropertyFormDialog(
            onDismissRequest = viewModel::hideFormDialog,
            onConfirmRequest = { name ->
                viewModel.createProperty(name)
                viewModel.hideFormDialog()
            }
        )
    }
}