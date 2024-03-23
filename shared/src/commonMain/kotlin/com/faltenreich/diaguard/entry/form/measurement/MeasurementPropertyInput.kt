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
import com.faltenreich.diaguard.shared.view.Divider
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.form.EntryFormIntent
import com.faltenreich.diaguard.food.eaten.FoodEatenInput
import com.faltenreich.diaguard.food.eaten.FoodEatenInputState
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyIcon
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.ResourceIcon

@Composable
fun MeasurementPropertyInput(
    state: MeasurementPropertyInputState,
    foodState: List<FoodEatenInputState>,
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
            MeasurementPropertyIcon(
                property = state.property,
                modifier = Modifier.padding(top = AppTheme.dimensions.padding.P_3_25),
            )
            FlowRow(modifier = modifier) {
                val types = state.typeInputStates
                types.forEach { type ->
                    MeasurementTypeInput(
                        data = type,
                        modifier = Modifier.weight(1f),
                        action = if (state.property.isMeal) {
                            {
                                IconButton(onClick = { onIntent(EntryFormIntent.SelectFood) }) {
                                    ResourceIcon(MR.images.ic_search)
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
        if (state.property.isMeal) {
            foodState.forEach { data ->
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