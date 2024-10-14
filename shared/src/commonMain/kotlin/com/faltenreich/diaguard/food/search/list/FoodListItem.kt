package com.faltenreich.diaguard.food.search.list

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
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.primitive.NumberFormatter

@Composable
fun FoodListItem(
    food: Food,
    modifier: Modifier = Modifier,
    // TODO: Extract into use case
    formatNumber: NumberFormatter = inject(),
    localization: Localization = inject(),
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .defaultMinSize(minHeight = AppTheme.dimensions.size.TouchSizeLarge)
            .padding(AppTheme.dimensions.padding.P_3),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = food.name,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = formatNumber(
                number = food.carbohydrates,
                scale = 2, // TODO: Get from preferences
                locale = localization.getLocale(),
            ),
            style = AppTheme.typography.bodyMedium,
        )
    }
}