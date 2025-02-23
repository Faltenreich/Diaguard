package com.faltenreich.diaguard.entry.form.measurement

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
    Card(modifier = modifier) {
        Row(
            modifier = Modifier
                .background(AppTheme.colors.scheme.surfaceContainerHigh)
                .fillMaxWidth()
                .padding(AppTheme.dimensions.padding.P_2_5),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MeasurementCategoryIcon(state.category)
            Text(
                text = state.category.name,
                color = AppTheme.colors.scheme.onSurface,
            )
        }
        val properties = state.propertyInputStates
        properties.forEachIndexed { index, property ->
            if (index != 0) {
                Divider()
            }
            MeasurementPropertyInput(
                data = property,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimensions.padding.P_0_5),
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
        if (state.category.isMeal) {
            foodEaten.forEach { data ->
                Divider()
                FormRow(
                    modifier = Modifier.background(AppTheme.colors.scheme.surfaceVariant),
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