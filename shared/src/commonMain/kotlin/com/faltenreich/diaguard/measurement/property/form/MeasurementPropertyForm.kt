package com.faltenreich.diaguard.measurement.property.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.TextInput
import com.faltenreich.diaguard.shared.view.TextInputDialog
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun MeasurementPropertyForm(
    modifier: Modifier = Modifier,
    viewModel: MeasurementPropertyFormViewModel = inject(),
) {
    val navigator = LocalNavigator.currentOrThrow
    val state = viewModel.viewState.collectAsState().value

    Column(modifier = modifier) {
        Column(
            modifier = Modifier.padding(all = AppTheme.dimensions.padding.P_3),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
        ) {
            TextInput(
                input = viewModel.name.collectAsState().value,
                onInputChange = { input -> viewModel.name.value = input },
                label = stringResource(MR.strings.name),
                modifier = Modifier.fillMaxWidth(),
            )
            TextInput(
                input = viewModel.icon.collectAsState().value,
                onInputChange = { input -> viewModel.icon.value = input },
                label = stringResource(MR.strings.icon),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_1),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Divider(modifier = Modifier.width(AppTheme.dimensions.padding.P_3_5))
            Text(
                text = stringResource(MR.strings.measurement_types),
                style = AppTheme.typography.bodySmall,
            )
            Divider(modifier = Modifier.weight(1f))
        }

        when (state) {
            is MeasurementPropertyFormViewState.Loading -> Unit
            is MeasurementPropertyFormViewState.Loaded -> LazyColumn {
                val listItems = state.types
                itemsIndexed(
                    items = listItems,
                    key = { _, item -> item.id },
                ) { index, item ->
                    MeasurementTypeListItem(
                        type = item,
                        onArrowUp = viewModel::decrementSortIndex.takeIf { index > 0 },
                        onArrowDown = viewModel::incrementSortIndex.takeIf { index < listItems.size - 1 },
                        modifier = Modifier
                            .animateItemPlacement()
                            .clickable { navigator.push(Screen.MeasurementTypeForm(item)) },
                    )
                }
            }
        }
    }

    if (state.showFormDialog) {
        TextInputDialog(
            title = stringResource(MR.strings.measurement_type_new),
            label = stringResource(MR.strings.name),
            onDismissRequest = viewModel::hideFormDialog,
            onConfirmRequest = { name ->
                viewModel.createType(name)
                viewModel.hideFormDialog()
            }
        )
    }
}