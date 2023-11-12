package com.faltenreich.diaguard.entry.form.measurement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.form.EntryFormIntent
import com.faltenreich.diaguard.food.eaten.FoodEatenInput
import com.faltenreich.diaguard.food.eaten.FoodEatenInputData
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyIcon
import com.faltenreich.diaguard.navigation.screen.FoodListScreen
import com.faltenreich.diaguard.shared.view.ResourceIcon

@Composable
fun MeasurementPropertyInput(
    data: MeasurementPropertyInputData,
    foodEaten: List<FoodEatenInputData>,
    onIntent: (EntryFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val navigator = LocalNavigator.currentOrThrow
    Row(
        modifier = modifier
            .defaultMinSize(minHeight = AppTheme.dimensions.size.TouchSizeLarge)
            .padding(AppTheme.dimensions.padding.P_3),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
        verticalAlignment = Alignment.Top,
    ) {
        MeasurementPropertyIcon(
            property = data.property,
            modifier = Modifier.padding(top = AppTheme.dimensions.padding.P_3_25),
        )
        FlowRow(modifier = modifier) {
            val types = data.typeInputDataList
            types.forEach { type ->
                MeasurementTypeInput(
                    data = type,
                    modifier = Modifier.weight(1f),
                    action =
                    if (data.property.isMeal) {
                        {
                            IconButton(
                                onClick = {
                                    navigator.push(FoodListScreen(onSelection = { food ->
                                        onIntent(EntryFormIntent.AddFood(food))
                                    }))
                                })
                            {
                                ResourceIcon(MR.images.ic_search)
                            }
                        }
                    } else {
                        null
                    },
                    onIntent = onIntent,
                )
            }
            if (data.property.isMeal) {
                foodEaten.forEach { data ->
                    FoodEatenInput(
                        data = data,
                        // TODO: modifier = Modifier.background(AppTheme.colors.scheme.background),
                        modifier = Modifier.fillMaxWidth(),
                        onIntent = onIntent,
                    )
                }
            }
        }
    }
}