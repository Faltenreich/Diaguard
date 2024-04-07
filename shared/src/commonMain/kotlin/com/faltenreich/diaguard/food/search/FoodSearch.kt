package com.faltenreich.diaguard.food.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.food.list.FoodList
import com.faltenreich.diaguard.food.list.FoodListSkeleton
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.carbohydrates_per_100g
import diaguard.shared.generated.resources.food

@Composable
fun FoodSearch(
    modifier: Modifier = Modifier,
    viewModel: FoodSearchViewModel = inject(),
) {
    val state = viewModel.collectState()
    Column {
        Row(
            modifier = Modifier
                .background(AppTheme.colors.scheme.primary)
                .padding(
                    start = AppTheme.dimensions.padding.P_3,
                    end = AppTheme.dimensions.padding.P_3,
                    bottom = AppTheme.dimensions.padding.P_2,
                ),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = getString(Res.string.food),
                modifier = Modifier.weight(1f),
                color = AppTheme.colors.scheme.onPrimary,
                style = AppTheme.typography.bodyMedium,
            )
            Text(
                text = getString(Res.string.carbohydrates_per_100g),
                color = AppTheme.colors.scheme.onPrimary,
                style = AppTheme.typography.bodyMedium,
            )
        }
        when (state) {
            is FoodSearchState.Loading, null -> FoodListSkeleton(modifier = modifier)
            is FoodSearchState.Loaded -> FoodList(
                items = state.foodList,
                onSelect = { viewModel.dispatchIntent(FoodSearchIntent.Select(it)) },
            )
        }
    }
}