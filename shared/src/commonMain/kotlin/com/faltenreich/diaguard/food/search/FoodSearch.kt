package com.faltenreich.diaguard.food.search

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.faltenreich.diaguard.data.food.Food
import com.faltenreich.diaguard.food.search.list.FoodList
import com.faltenreich.diaguard.food.search.list.FoodListEmpty
import com.faltenreich.diaguard.food.search.list.FoodListSkeleton
import com.faltenreich.diaguard.view.LifecycleState
import com.faltenreich.diaguard.view.PullToRefresh
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import com.faltenreich.diaguard.view.rememberLifecycleState
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FoodSearch(
    state: FoodSearchState?,
    onSelect: (Food.Local) -> Unit,
    modifier: Modifier = Modifier,
) {
    val items = state?.pagingData?.collectAsLazyPagingItems()
    val isLoading = items == null || items.loadState.refresh == LoadState.Loading
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(isLoading) {
        if (!isLoading) {
            isRefreshing = false
        }
    }

    val lifecycleState = rememberLifecycleState()
    LaunchedEffect(lifecycleState) {
        if (lifecycleState == LifecycleState.RESUMED) {
            items?.refresh()
        }
    }

    Column(modifier = modifier) {
        FoodSearchHeader()

        PullToRefresh(
            isRefreshing = isRefreshing && isLoading,
            onRefresh = {
                isRefreshing = true
                items?.refresh()
            },
        ) {
            if (isLoading) {
                FoodListSkeleton()
            } else {
                val isAtTheEnd = items.loadState.refresh !is LoadState.Loading
                    && items.loadState.prepend !is LoadState.Loading
                    && items.loadState.append !is LoadState.Loading
                if (isAtTheEnd && items.itemCount == 0) {
                    FoodListEmpty()
                } else {
                    FoodList(
                        items = items,
                        onSelect = onSelect,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    FoodSearch(
        state = FoodSearchState(
            query = "Query",
            pagingData = flowOf(PagingData.from(listOf(food().localized()))),
        ),
        onSelect = {},
    )
}