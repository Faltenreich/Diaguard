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
import com.faltenreich.diaguard.view.divider.Divider
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MeasurementCategoryList(
    state: MeasurementCategoryListState?,
    onIntent: (MeasurementCategoryListIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
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
                        onIntent = onIntent,
                        showArrowUp = index > 1,
                        showArrowDown = index > 0 && index < categories.size - 1,
                        modifier = Modifier
                            .animateItem()
                            .clickable { onIntent(MeasurementCategoryListIntent.Edit(category)) },
                    )
                }
            }
        }
    }

    if (state?.formDialog != null) {
        MeasurementCategoryFormDialog(
            onDismissRequest = { onIntent(MeasurementCategoryListIntent.CloseFormDialog) },
            onConfirmRequest = { name ->
                onIntent(MeasurementCategoryListIntent.CloseFormDialog)
                onIntent(MeasurementCategoryListIntent.Create(name))
            },
        )
    }
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    MeasurementCategoryList(
        state = MeasurementCategoryListState(
            categories = listOf(category()),
            formDialog = null,
        ),
        onIntent = {},
    )
}