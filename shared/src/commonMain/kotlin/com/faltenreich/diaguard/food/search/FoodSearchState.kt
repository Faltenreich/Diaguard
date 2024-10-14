package com.faltenreich.diaguard.food.search

import androidx.paging.PagingData
import com.faltenreich.diaguard.food.Food
import kotlinx.coroutines.flow.Flow

data class FoodSearchState(
    val pagingData: Flow<PagingData<Food.Local>>
)