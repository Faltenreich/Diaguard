package com.faltenreich.diaguard.measurement.type.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.navigation.screen.MeasurementTypeFormScreen
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.TextDivider

@Suppress("FunctionName")
fun LazyListScope.MeasurementTypeList(
    types: List<MeasurementType>,
    viewModel: MeasurementTypeListViewModel = inject(),
) {
    item {
        TextDivider(getString(MR.strings.measurement_types))
    }
    itemsIndexed(
        items = types,
        key = { _, item -> item.id },
    ) { index, type ->
        val navigator = LocalNavigator.currentOrThrow
        MeasurementTypeListItem(
            type = type,
            onArrowUp = { viewModel.decrementSortIndex(type, types) },
            showArrowUp = index > 0,
            onArrowDown = { viewModel.incrementSortIndex(type, types) },
            showArrowDown = index < types.size - 1,
            modifier = Modifier
                .animateItemPlacement()
                .clickable { navigator.push(MeasurementTypeFormScreen(type.id)) },
        )
    }
}