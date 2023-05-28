package com.faltenreich.diaguard.log.usecase

import com.faltenreich.diaguard.log.item.LogItem
import com.faltenreich.diaguard.shared.datetime.MonthOfYear
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MapLogItemsUseCase(
    private val dispatcher: CoroutineDispatcher,
) {

    suspend operator fun invoke(
        items: List<LogItem>,
    ): Map<MonthOfYear, List<LogItem>> = withContext(dispatcher) {
        items
            .groupBy { item -> item.date.monthOfYear }
            .map { (monthOfYear, itemsOfMonth) -> monthOfYear to itemsOfMonth }
            .toMap()
    }
}