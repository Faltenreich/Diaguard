package com.faltenreich.diaguard.measurement.unit.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.data.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.form.MeasurementUnitFormDialog
import com.faltenreich.diaguard.view.divider.Divider
import com.faltenreich.diaguard.data.preview.AppPreview
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MeasurementUnitList(
    state: MeasurementUnitListState?,
    onIntent: (MeasurementUnitListIntent) -> Unit,
    onSelect: (MeasurementUnit.Local) -> Unit,
    modifier: Modifier = Modifier,
) {
    state ?: return

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
                        .clickable { onSelect(unit) },
                )
            }
        }
    }

    state.formDialog?.let { formDialog ->
        MeasurementUnitFormDialog(
            unit = formDialog.unit,
            error = formDialog.error,
            onDismissRequest = { onIntent(MeasurementUnitListIntent.CloseFormDialog) },
            onConfirmRequest = { name, abbreviation ->
                onIntent(
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

@Preview
@Composable
private fun Preview() = AppPreview {
    MeasurementUnitList(
        state = MeasurementUnitListState(
            units = listOf(unit()),
            formDialog = null,
        ),
        onIntent = {},
        onSelect = {},
    )
}