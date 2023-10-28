package com.faltenreich.diaguard.food.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun FoodList(
    modifier: Modifier = Modifier,
    viewModel: FoodListViewModel = inject(),
) {
    val viewState = viewModel.viewState.collectAsState().value
    val shimmerInstance = rememberShimmer(ShimmerBounds.Custom)
    Column {
        Row(
            modifier = Modifier
                .background(AppTheme.colors.scheme.surfaceVariant)
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
                style = AppTheme.typography.bodyMedium,
            )
            Text(
                text = getString(MR.strings.carbohydrates_per_100g),
                style = AppTheme.typography.bodyMedium,
            )
        }
        LazyColumn(modifier = modifier) {
            when (viewState) {
                is FoodListViewState.Loading -> items(10) {
                    Column {
                        Row(
                            modifier = modifier
                                .height(AppTheme.dimensions.size.TouchSizeLarge)
                                .padding(all = AppTheme.dimensions.padding.P_3),
                            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f)
                                    .shimmer(shimmerInstance)
                                    .background(AppTheme.colors.scheme.surfaceVariant),
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(AppTheme.dimensions.size.TouchSizeMedium)
                                    .shimmer(shimmerInstance)
                                    .background(AppTheme.colors.scheme.surfaceVariant),
                            )
                        }
                        Divider()
                    }
                }
                is FoodListViewState.Result -> items(viewState.items, key = Food::id) { food ->
                    Column {
                        FoodListItem(food)
                        Divider()
                    }
                }
            }
        }
    }
}