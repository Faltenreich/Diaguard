package com.faltenreich.diaguard.food.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.view.Divider

@Composable
fun FoodList(
    items: List<Food>,
    onRefresh: () -> Unit,
    onSelect: (Food) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pullToRefreshState = rememberPullToRefreshState()
    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            onRefresh()
            // TODO: pullToRefreshState.endRefresh()
        }
    }
    Box(modifier = modifier.nestedScroll(pullToRefreshState.nestedScrollConnection)) {
        LazyColumn {
            items(items, key = Food::id) { food ->
                Column {
                    FoodListItem(
                        food = food,
                        modifier = Modifier.clickable { onSelect(food) },
                    )
                    Divider()
                }
            }
        }
        PullToRefreshContainer(
            modifier = Modifier.align(Alignment.TopCenter),
            state = pullToRefreshState,
        )
    }
}