package com.faltenreich.diaguard.entry.form.measurement

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.entry.form.EntryFormIntent
import com.faltenreich.diaguard.food.eaten.FoodEatenInput
import com.faltenreich.diaguard.food.eaten.FoodEatenInputState
import com.faltenreich.diaguard.measurement.category.icon.MeasurementCategoryIcon
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.ResourceIcon
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_search

@Composable
fun MeasurementCategoryInput(
    state: MeasurementCategoryInputState,
    foodEaten: List<FoodEatenInputState>,
    onIntent: (EntryFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column {
        Row(
            modifier = modifier
                .defaultMinSize(minHeight = AppTheme.dimensions.size.TouchSizeLarge)
                .padding(AppTheme.dimensions.padding.P_3),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
            verticalAlignment = Alignment.Top,
        ) {
            MeasurementCategoryIcon(
                category = state.category,
                modifier = Modifier.padding(top = AppTheme.dimensions.padding.P_3_25),
            )
            FlowRow(modifier = modifier) {
                val properties = state.propertyInputStates
                properties.forEach { property ->
                    MeasurementPropertyInput(
                        data = property,
                        modifier = Modifier.weight(1f),
                        action = if (state.category.isMeal) {
                            {
                                IconButton(onClick = { onIntent(EntryFormIntent.SelectFood) }) {
                                    ResourceIcon(Res.drawable.ic_search)
                                }
                            }
                        } else {
                            null
                        },
                        onIntent = onIntent,
                    )
                }
            }
        }
        if (state.category.isMeal) {
            foodEaten.forEach { data ->
                Divider()
                FormRow(
                    modifier = modifier.background(AppTheme.colors.scheme.surfaceVariant),
                    icon = { Spacer(modifier = Modifier.width(AppTheme.dimensions.size.ImageMedium)) }
                ) {
                    FoodEatenInput(
                        data = data,
                        modifier = Modifier.fillMaxWidth(),
                        onIntent = onIntent,
                    )
                }
            }
        }
    }
}