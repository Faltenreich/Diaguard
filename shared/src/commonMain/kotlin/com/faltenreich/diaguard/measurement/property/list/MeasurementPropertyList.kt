package com.faltenreich.diaguard.measurement.property.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.Dialog
import com.faltenreich.diaguard.shared.view.TextInput
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
                    modifier = Modifier
                        .animateItemPlacement()
                        .clickable { navigator.push(Screen.MeasurementPropertyForm(item)) },
                )
            }
        }
    }
    if (state.showFormDialog) {
        var name by mutableStateOf("")
        Dialog(
            onDismissRequest = viewModel::hideFormDialog,
            confirmButton = {
                TextButton(onClick = {
                    viewModel.createProperty(name)
                    viewModel.hideFormDialog()
                }) {
                    Text(stringResource(MR.strings.create))
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::hideFormDialog) {
                    Text(stringResource(MR.strings.cancel))
                }
            },
            title = { Text(stringResource(MR.strings.measurement_property_new)) },
            text = {
                TextInput(
                    input = name,
                    onInputChange = { name = it },
                    label = stringResource(MR.strings.name),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        )
    }
}