package com.faltenreich.diaguard.measurement.type.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.TextDivider

@Suppress("FunctionName")
fun LazyListScope.MeasurementTypeList(
    types: List<MeasurementType>,
    viewModel: MeasurementTypeListViewModel = inject(),
) {
    stickyHeader { TextDivider(getString(MR.strings.measurement_types)) }

    itemsIndexed(
        items = types,
        key = { _, item -> item.id },
    ) { index, type ->
        MeasurementTypeListItem(
            type = type,
            onArrowUp = { viewModel.dispatchIntent(MeasurementTypeListIntent.DecrementSortIndex(type, types)) },
            showArrowUp = index > 0,
            onArrowDown = { viewModel.dispatchIntent(MeasurementTypeListIntent.IncrementSortIndex(type, types)) },
            showArrowDown = index < types.size - 1,
            modifier = Modifier
                .animateItemPlacement()
                .clickable { viewModel.dispatchIntent(MeasurementTypeListIntent.EditType(type)) },
        )
    }
}