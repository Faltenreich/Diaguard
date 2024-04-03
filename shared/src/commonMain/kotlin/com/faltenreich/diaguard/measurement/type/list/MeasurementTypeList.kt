package com.faltenreich.diaguard.measurement.type.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.TextDivider

@Composable
fun MeasurementTypeList(
    types: List<MeasurementType>,
    viewModel: MeasurementTypeListViewModel = inject(),
) {
    Column {
        TextDivider(getString(MR.strings.measurement_types))
        types.forEachIndexed { index, type ->
            MeasurementTypeListItem(
                type = type,
                onArrowUp = { viewModel.dispatchIntent(MeasurementTypeListIntent.DecrementSortIndex(type, types)) },
                showArrowUp = index > 0,
                onArrowDown = { viewModel.dispatchIntent(MeasurementTypeListIntent.IncrementSortIndex(type, types)) },
                showArrowDown = index < types.size - 1,
                modifier = Modifier.clickable { viewModel.dispatchIntent(MeasurementTypeListIntent.EditType(type)) },
            )
        }
    }
}