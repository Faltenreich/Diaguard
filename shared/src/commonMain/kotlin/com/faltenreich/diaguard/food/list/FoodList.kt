package com.faltenreich.diaguard.food.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.paging.LoadState
import app.cash.paging.compose.LazyPagingItems
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.view.Divider

@Composable
fun FoodList(
    items: LazyPagingItems<Food.Local>,
    onRefresh: () -> Unit,
    onSelect: (Food.Local) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pullToRefreshState = rememberPullToRefreshState()
    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            onRefresh()
            pullToRefreshState.endRefresh()
        }
    }
    Box(modifier = modifier.nestedScroll(pullToRefreshState.nestedScrollConnection)) {
        LazyColumn {
            for (index in 0 until items.itemCount) {
                item {
                    val food = items[index]
                    if (food != null) {
                        Column {
                            FoodListItem(
                                food = food,
                                modifier = Modifier.clickable { onSelect(food) },
                            )
                            Divider()
                        }
                    }
                }
            }

            if (items.loadState.append == LoadState.Loading) {
                item {
                    FoodListLoadingIndicator()
                }
            }
        }
        PullToRefreshContainer(
            modifier = Modifier.align(Alignment.TopCenter),
            state = pullToRefreshState,
        )
    }
}