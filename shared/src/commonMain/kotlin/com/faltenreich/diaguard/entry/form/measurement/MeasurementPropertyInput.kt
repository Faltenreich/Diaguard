package com.faltenreich.diaguard.entry.form.measurement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.form.EntryFormIntent
import com.faltenreich.diaguard.food.eaten.FoodEatenInputData
import com.faltenreich.diaguard.food.eaten.FoodEatenInput
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyIcon
import com.faltenreich.diaguard.navigation.screen.FoodListScreen
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.ResourceIcon

@Composable
fun MeasurementPropertyInput(
    data: MeasurementPropertyInputData,
    foodEaten: List<FoodEatenInputData>,
    onIntent: (EntryFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val navigator = LocalNavigator.currentOrThrow
    Column {
        FormRow(
            icon = { MeasurementPropertyIcon(data.property) },
            modifier = modifier,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_1)) {
                val types = data.typeInputDataList
                types.forEach { type ->
                    MeasurementTypeInput(
                        data = type,
                        action = {
                            if (data.property.isMeal) {
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
                        },
                        onIntent = onIntent,
                    )
                }
            }
        }
        if (data.property.isMeal) {
            foodEaten.forEach { data ->
                Divider()
                FoodEatenInput(
                    data = data,
                    // TODO: modifier = Modifier.background(AppTheme.colors.scheme.background),
                    onIntent = onIntent,
                )
            }
        }
        Divider()
    }
}