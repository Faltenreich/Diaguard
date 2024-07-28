package com.faltenreich.diaguard.food.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import app.cash.paging.compose.collectAsLazyPagingItems
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.food.search.list.FoodList
import com.faltenreich.diaguard.food.search.list.FoodListEmpty
import com.faltenreich.diaguard.food.search.list.FoodListSkeleton

@Composable
fun FoodSearch(
    modifier: Modifier = Modifier,
    viewModel: FoodSearchViewModel,
) {
    val state = viewModel.collectState() ?: return
    val items = state.pagingData.collectAsLazyPagingItems()

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
            FoodListEmpty()
        } else {
            FoodList(
                items = items,
                onSelect = { food -> viewModel.dispatchIntent(FoodSearchIntent.Select(food)) },
            )
        }
    }
}