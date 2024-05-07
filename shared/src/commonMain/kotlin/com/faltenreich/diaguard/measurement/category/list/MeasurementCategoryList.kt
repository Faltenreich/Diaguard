package com.faltenreich.diaguard.measurement.category.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
        modifier = modifier,
        enter = fadeIn(),
    ) {
        val categories = state?.categories ?: emptyList()

        LazyColumn {
            itemsIndexed(categories, key = { _, category -> category.id }) { index, category ->
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
                    modifier = Modifier
                        .animateItemPlacement()
                        .clickable { viewModel.dispatchIntent(MeasurementCategoryListIntent.Edit(category)) },
                )
            }
        }
    }
}