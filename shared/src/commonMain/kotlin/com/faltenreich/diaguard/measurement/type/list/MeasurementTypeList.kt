package com.faltenreich.diaguard.measurement.type.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.measurement.property.form.MeasurementTypeListItem
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.shared.di.inject

@Suppress("FunctionName")
fun LazyListScope.MeasurementTypeList(
    types: List<MeasurementType>,
    viewModel: MeasurementTypeListViewModel = inject(),
) {
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
                .clickable { navigator.push(Screen.MeasurementTypeForm(type.id)) },
        )
    }
}