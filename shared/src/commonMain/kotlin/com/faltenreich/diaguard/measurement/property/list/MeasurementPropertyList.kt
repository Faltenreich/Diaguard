package com.faltenreich.diaguard.measurement.property.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.TextInputDialog
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun MeasurementPropertyList(
    modifier: Modifier = Modifier,
    viewModel: MeasurementPropertyListViewModel = inject(),
) {
    val navigator = LocalNavigator.currentOrThrow
    val state = viewModel.viewState.collectAsState().value
    when (state) {
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
                    onDelete = viewModel::deleteProperty,
                    modifier = Modifier
                        .animateItemPlacement()
                        .clickable { navigator.push(Screen.MeasurementPropertyForm(item)) },
                )
            }
        }
    }
    if (state.showFormDialog) {
        TextInputDialog(
            title = stringResource(MR.strings.measurement_property_new),
            label = stringResource(MR.strings.name),
            onDismissRequest = viewModel::hideFormDialog,
            onConfirmRequest = { name ->
                viewModel.createProperty(name)
                viewModel.hideFormDialog()
            }
        )
    }
}