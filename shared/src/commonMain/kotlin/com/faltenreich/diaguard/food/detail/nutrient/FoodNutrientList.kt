package com.faltenreich.diaguard.food.detail.nutrient

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString

@Composable
fun FoodNutrientList(
    modifier: Modifier = Modifier,
    viewModel: FoodNutrientListViewModel = inject(),
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Row(
            modifier = Modifier
                .padding(
                    start = AppTheme.dimensions.padding.P_3,
                    top = AppTheme.dimensions.padding.P_3,
                    end = AppTheme.dimensions.padding.P_3,
                    bottom = AppTheme.dimensions.padding.P_1,
                ),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = getString(MR.strings.nutrient),
                modifier = Modifier.weight(1f),
                style = AppTheme.typography.bodyMedium,
            )
            Text(
                text = getString(MR.strings.per_100g),
                style = AppTheme.typography.bodyMedium,
            )
        }
        viewModel.data.forEach { data ->
            FoodNutrientListItem(data)
            Divider()
        }
    }
}