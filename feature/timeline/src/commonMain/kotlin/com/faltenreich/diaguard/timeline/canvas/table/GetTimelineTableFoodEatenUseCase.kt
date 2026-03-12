package com.faltenreich.diaguard.timeline.canvas.table

import com.faltenreich.diaguard.data.food.eaten.FoodEaten
import com.faltenreich.diaguard.data.food.eaten.FoodEatenRepository
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateUnit
import kotlinx.coroutines.flow.Flow

class GetTimelineTableFoodEatenUseCase(
    private val repository: FoodEatenRepository,
) {

    operator fun invoke(date: Date): Flow<List<FoodEaten.Local>> {
        return repository.observeByDateRange(
            startDateTime = date.minus(1, DateUnit.DAY).atStartOfDay(),
            endDateTime = date.plus(1, DateUnit.DAY).atEndOfDay(),
        )
    }
}