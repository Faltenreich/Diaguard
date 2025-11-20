package com.faltenreich.diaguard.entry.form.measurement

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.view.theme.AppTheme
import com.faltenreich.diaguard.entry.form.EntryFormIntent
import com.faltenreich.diaguard.food.eaten.FoodEatenInput
import com.faltenreich.diaguard.food.eaten.FoodEatenInputState
import com.faltenreich.diaguard.measurement.category.icon.MeasurementCategoryIcon
import com.faltenreich.diaguard.view.Divider
import com.faltenreich.diaguard.view.ResourceIcon
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.food_add
import diaguard.shared.generated.resources.ic_search
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MeasurementCategoryInput(
    state: MeasurementCategoryInputState,
    foodEaten: List<FoodEatenInputState>,
    onIntent: (EntryFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.animateContentSize()) {
        Row(
            modifier = Modifier
                .background(AppTheme.colors.scheme.surfaceContainerLow)
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
            Row(
                modifier = Modifier.padding(AppTheme.dimensions.padding.P_0_5),
                horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MeasurementPropertyInput(
                    state = property,
                    modifier = Modifier.weight(1f),
                    onIntent = onIntent,
                )
                if (state.category.isMeal) {
                    IconButton(
                        onClick = { onIntent(EntryFormIntent.SelectFood) },
                        modifier = Modifier.padding(end = AppTheme.dimensions.padding.P_1),
                    ) {
                        ResourceIcon(
                            icon = Res.drawable.ic_search,
                            contentDescription = stringResource(Res.string.food_add),
                        )
                    }
                }
            }
            property.error?.let { error ->
                Text(
                    text = error,
                    modifier = Modifier.padding(
                        start = AppTheme.dimensions.padding.P_3,
                        end = AppTheme.dimensions.padding.P_3,
                        bottom = AppTheme.dimensions.padding.P_2,
                    ),
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.colors.scheme.error,
                )
            }
        }
        if (state.category.isMeal) {
            foodEaten.forEachIndexed { index, data ->
                Divider()
                FoodEatenInput(
                    state = data,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppTheme.dimensions.padding.P_1),
                    onIntent = onIntent,
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    MeasurementCategoryInput(
        state = MeasurementCategoryInputState(
            category = category(),
            propertyInputStates = listOf(
                MeasurementPropertyInputState(
                    property = property(),
                    input = "",
                    isLast = true,
                    error = null,
                    decimalPlaces = 3,
                ),
            ),
        ),
        foodEaten = emptyList(),
        onIntent = {},
    )
}