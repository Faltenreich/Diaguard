package com.faltenreich.diaguard.food.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.measurement.value.MeasurementValueFormatter
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.Skeleton

@Composable
fun FoodListItem(
    food: Food?,
    modifier: Modifier = Modifier,
    valueFormatter: MeasurementValueFormatter = inject(),
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .defaultMinSize(minHeight = AppTheme.dimensions.size.TouchSizeLarge)
            .padding(AppTheme.dimensions.padding.P_3),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Skeleton(
            item = food,
            modifier = Modifier.weight(1f),
        ) { food ->
            Text(food.name)
        }
        Skeleton(item = food) { food ->
            Text(
                text = valueFormatter.formatValue(food.carbohydrates, factor = 1.0),
                style = AppTheme.typography.bodyMedium,
            )
        }
    }
}