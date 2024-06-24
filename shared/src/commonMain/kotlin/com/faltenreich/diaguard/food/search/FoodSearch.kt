package com.faltenreich.diaguard.food.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.paging.LoadState
import app.cash.paging.compose.collectAsLazyPagingItems
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.food.list.FoodList
import com.faltenreich.diaguard.food.list.FoodListSkeleton
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun FoodSearch(
    modifier: Modifier = Modifier,
    viewModel: FoodSearchViewModel = inject(),
) {
    val items = viewModel.pagingData.collectAsLazyPagingItems()

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

        val isAtTheEnd = items.loadState.refresh !is LoadState.Loading
            && items.loadState.prepend !is LoadState.Loading
            && items.loadState.append !is LoadState.Loading
        
        if (items.loadState.refresh == LoadState.Loading) {
            FoodListSkeleton()
        } else if (isAtTheEnd && items.itemCount == 0) {
            FoodSearchEmpty()
        } else {
            FoodList(
                items = items,
                onRefresh = { viewModel.dispatchIntent(FoodSearchIntent.Refresh) },
                onSelect = { food -> viewModel.dispatchIntent(FoodSearchIntent.Select(food)) },
                // Workaround to place PullToRefreshContainer behind headers
                modifier = Modifier.zIndex(-1f),
            )
        }
    }
}