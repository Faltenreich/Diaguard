package com.faltenreich.diaguard.measurement.unit.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import diaguard.shared.generated.resources.*
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.TextDivider

@Composable
fun MeasurementUnitList(
    units: List<MeasurementUnit>,
    modifier: Modifier = Modifier,
    viewModel: MeasurementUnitListViewModel = inject(),
) {
    Column(modifier = modifier) {
        TextDivider(getString(Res.string.measurement_units))
        units.forEach { unit ->
            MeasurementUnitListItem(
                unit = unit,
                modifier = Modifier.clickable {
                    viewModel.dispatchIntent(MeasurementUnitListIntent.Select(unit))
                },
            )
        }
    }
}