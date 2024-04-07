package com.faltenreich.diaguard.food.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.food.list.FoodList
import com.faltenreich.diaguard.food.list.FoodListSkeleton
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun FoodSearch(
    modifier: Modifier = Modifier,
    viewModel: FoodSearchViewModel = inject(),
) {
    val state = viewModel.collectState()

    Column(modifier = modifier) {
        Column(modifier = Modifier.background(AppTheme.colors.scheme.primary)) {
            FoodSearchField(
                query = viewModel.query,
                onQueryChange = { viewModel.query = it },
                onNavigateBack = { viewModel.dispatchIntent(FoodSearchIntent.Close) },
            )
            FoodSearchHeader(
                modifier = Modifier
                    .padding(
                        start = AppTheme.dimensions.padding.P_3,
                        end = AppTheme.dimensions.padding.P_3,
                        bottom = AppTheme.dimensions.padding.P_2,
                    ),
            )
        }
        when (state) {
            is FoodSearchState.Loading, null -> FoodListSkeleton()
            is FoodSearchState.Loaded -> FoodList(
                items = state.items,
                onSelect = { viewModel.dispatchIntent(FoodSearchIntent.Select(it)) },
            )
        }
    }
}