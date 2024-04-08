package com.faltenreich.diaguard.measurement.category.list

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
fun MeasurementCategoryList(
    modifier: Modifier = Modifier,
    viewModel: MeasurementCategoryListViewModel = inject(),
) {
    val state = viewModel.collectState()

    AnimatedVisibility(
        visible = state != null,
        enter = fadeIn(),
    ) {
        val categories = state?.categories ?: emptyList()
        Column(modifier = modifier.verticalScroll(rememberScrollState())) {
            categories.forEachIndexed { index, category ->
                MeasurementCategoryListItem(
                    category = category,
                    onArrowUp = {
                        viewModel.dispatchIntent(
                            MeasurementCategoryListIntent.DecrementSortIndex(category)
                        )
                    },
                    showArrowUp = index > 0,
                    onArrowDown = {
                        viewModel.dispatchIntent(
                            MeasurementCategoryListIntent.IncrementSortIndex(category)
                        )
                    },
                    showArrowDown = index < categories.size - 1,
                    modifier = Modifier.clickable {
                        viewModel.dispatchIntent(MeasurementCategoryListIntent.Edit(category))
                    },
                )
            }
        }
    }
}