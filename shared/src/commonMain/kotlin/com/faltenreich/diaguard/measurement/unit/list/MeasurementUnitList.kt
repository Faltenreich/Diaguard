package com.faltenreich.diaguard.measurement.unit.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.measurement.unit.form.MeasurementUnitFormDialog
import com.faltenreich.diaguard.shared.view.Divider

@Composable
fun MeasurementUnitList(
    viewModel: MeasurementUnitListViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.collectState() ?: return

    LazyColumn(modifier = modifier) {
        itemsIndexed(state.units, key = { _, item -> item.id }) { index, unit ->
            Column {
                if (index != 0) {
                    Divider()
                }
                MeasurementUnitListItem(
                    unit = unit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.dispatchIntent(MeasurementUnitListIntent.OpenFormDialog(unit)) },
                )
            }
        }
    }

    state.formDialog?.let { formDialog ->
        MeasurementUnitFormDialog(
            unit = formDialog.unit,
            onDismissRequest = { viewModel.dispatchIntent(MeasurementUnitListIntent.CloseFormDialog) },
            onConfirmRequest = { name, abbreviation ->
                viewModel.dispatchIntent(
                    MeasurementUnitListIntent.StoreUnit(
                        unit = formDialog.unit,
                        name = name,
                        abbreviation = abbreviation,
                    )
                )
            }
        )
    }
}