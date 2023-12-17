package com.faltenreich.diaguard.measurement.unit.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.measurement.type.list.MeasurementUnitListIntent
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.TextDivider

@Suppress("FunctionName")
fun LazyListScope.MeasurementUnitList(
    units: List<MeasurementUnit>,
    viewModel: MeasurementUnitListViewModel = inject(),
) {
    item {
        TextDivider(getString(MR.strings.measurement_units))
    }
    items(
        items = units,
        key = MeasurementUnit::id,
    ) { unit ->
        MeasurementUnitListItem(
            unit = unit,
            modifier = Modifier
                .animateItemPlacement()
                .clickable { viewModel.dispatchIntent(MeasurementUnitListIntent.Select(unit)) },
        )
    }
}