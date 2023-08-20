package com.faltenreich.diaguard.measurement.property.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.TextInput
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun MeasurementPropertyForm(
    modifier: Modifier = Modifier,
    viewModel: MeasurementPropertyFormViewModel = inject(),
) {
    val state = viewModel.viewState.collectAsState().value
    Column(modifier = modifier) {
        Column(
            modifier = Modifier.padding(all = AppTheme.dimensions.padding.P_3),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
        ) {
            TextInput(
                input = viewModel.name,
                onInputChange = { input -> viewModel.name = input },
                label = stringResource(MR.strings.name),
                modifier = Modifier.fillMaxWidth(),
            )
            TextInput(
                input = viewModel.icon,
                onInputChange = { input -> viewModel.icon = input },
                label = stringResource(MR.strings.icon),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Divider()

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
                        modifier = Modifier.animateItemPlacement(),
                    )
                }
            }
        }
    }
}