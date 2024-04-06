package com.faltenreich.diaguard.measurement.property.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun MeasurementPropertyList(
    modifier: Modifier = Modifier,
    viewModel: MeasurementPropertyListViewModel = inject(),
) {
    val state = viewModel.collectState()
    val properties = state?.properties ?: emptyList()

    AnimatedVisibility(
        visible = properties.isNotEmpty(),
        enter = fadeIn(),
    ) {
        Column(modifier = modifier.verticalScroll(rememberScrollState())) {
            properties.forEachIndexed { index, property ->
                MeasurementPropertyListItem(
                    property = property,
                    onArrowUp = {
                        viewModel.dispatchIntent(
                            MeasurementPropertyListIntent.DecrementSortIndex(property)
                        )
                    },
                    showArrowUp = index > 0,
                    onArrowDown = {
                        viewModel.dispatchIntent(
                            MeasurementPropertyListIntent.IncrementSortIndex(property)
                        )
                    },
                    showArrowDown = index < properties.size - 1,
                    modifier = Modifier.clickable {
                        viewModel.dispatchIntent(MeasurementPropertyListIntent.Edit(property))
                    },
                )
            }
        }
    }
}