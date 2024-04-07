package com.faltenreich.diaguard.food.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.skeleton
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.carbohydrates_per_100g
import diaguard.shared.generated.resources.food

@Composable
fun FoodList(
    modifier: Modifier = Modifier,
    viewModel: FoodListViewModel = inject(),
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
            is FoodListState.Loading, null -> Column(modifier) {
                repeat(10) {
                    Row(
                        modifier = modifier
                            .height(IntrinsicSize.Min)
                            .defaultMinSize(minHeight = AppTheme.dimensions.size.TouchSizeLarge)
                            .padding(AppTheme.dimensions.padding.P_3),
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(modifier = Modifier.weight(1f).skeleton(true))
                        Box(modifier = Modifier.width(50.dp).skeleton(true))
                    }
                    Divider()
                }
            }
            is FoodListState.Loaded -> LazyColumn(modifier = modifier) {
                items(state.foodList, key = Food::id) { food ->
                    Column {
                        FoodListItem(
                            food = food,
                            modifier = Modifier.clickable {
                                viewModel.dispatchIntent(FoodListIntent.Select(food))
                            }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}