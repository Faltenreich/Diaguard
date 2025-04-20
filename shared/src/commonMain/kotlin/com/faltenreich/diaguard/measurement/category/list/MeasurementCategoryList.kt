package com.faltenreich.diaguard.measurement.category.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.measurement.category.form.MeasurementCategoryFormDialog
import com.faltenreich.diaguard.shared.view.Divider

@Composable
fun MeasurementCategoryList(
    viewModel: MeasurementCategoryListViewModel,
    modifier: Modifier = Modifier,
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
                Column {
                    if (index != 0) {
                        Divider()
                    }
                    MeasurementCategoryListItem(
                        category = category,
                        onIntent = { intent -> viewModel.dispatchIntent(intent) },
                        showArrowUp = index > 1,
                        showArrowDown = index > 0 && index < categories.size - 1,
                        modifier = Modifier
                            .animateItem()
                            .clickable { viewModel.dispatchIntent(MeasurementCategoryListIntent.Edit(category)) },
                    )
                }
            }
        }
    }

    if (state?.formDialog != null) {
        MeasurementCategoryFormDialog(
            onDismissRequest = { viewModel.dispatchIntent(MeasurementCategoryListIntent.CloseFormDialog) },
            onConfirmRequest = { name ->
                viewModel.dispatchIntent(MeasurementCategoryListIntent.CloseFormDialog)
                viewModel.dispatchIntent(MeasurementCategoryListIntent.Create(name))
            },
        )
    }
}