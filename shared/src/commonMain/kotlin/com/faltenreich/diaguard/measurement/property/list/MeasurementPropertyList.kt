package com.faltenreich.diaguard.measurement.property.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormDialog
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.screen.MeasurementPropertyFormScreen
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.LoadingIndicator

@Composable
fun MeasurementPropertyList(
    modifier: Modifier = Modifier,
    viewModel: MeasurementPropertyListViewModel = inject(),
    navigation: Navigation = inject(),
) {
    when (val state = viewModel.collectState()) {
        null -> LoadingIndicator(modifier = modifier)
        is MeasurementPropertyListViewState.Loaded -> Box(modifier = modifier) {
            LazyColumn {
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
                            .clickable { navigation.push(MeasurementPropertyFormScreen(item)) },
                    )
                }
            }
            if (state.showFormDialog) {
                MeasurementPropertyFormDialog(
                    onDismissRequest = viewModel::hideFormDialog,
                    onConfirmRequest = { name ->
                        viewModel.createProperty(name, other = state.listItems)
                        viewModel.hideFormDialog()
                    }
                )
            }
        }
    }
}