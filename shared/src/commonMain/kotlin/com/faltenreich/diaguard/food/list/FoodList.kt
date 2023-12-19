package com.faltenreich.diaguard.food.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.itemsElse

@Composable
fun FoodList(
    onSelection: ((Food) -> Unit)? = null,
    modifier: Modifier = Modifier,
    viewModel: FoodListViewModel = inject(),
) {
    val items = viewModel.collectState()
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
                text = getString(MR.strings.food),
                modifier = Modifier.weight(1f),
                color = AppTheme.colors.scheme.onPrimary,
                style = AppTheme.typography.bodyMedium,
            )
            Text(
                text = getString(MR.strings.carbohydrates_per_100g),
                color = AppTheme.colors.scheme.onPrimary,
                style = AppTheme.typography.bodyMedium,
            )
        }
        LazyColumn(modifier = modifier) {
            itemsElse(items, key = Food::id) { food ->
                Column {
                    FoodListItem(
                        food = food,
                        modifier = Modifier
                            .clickable {
                                food ?: return@clickable
                                onSelection?.let {
                                    onSelection(food)
                                    viewModel.dispatchIntent(FoodListIntent.Close)
                                } ?: viewModel.dispatchIntent(FoodListIntent.EditFood(food))
                            }
                    )
                    Divider()
                }
            }
        }
    }
}