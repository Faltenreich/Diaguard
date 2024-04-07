package com.faltenreich.diaguard.food.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.view.Divider

@Composable
fun FoodList(
    items: List<Food>,
    onSelect: (Food) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
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
}