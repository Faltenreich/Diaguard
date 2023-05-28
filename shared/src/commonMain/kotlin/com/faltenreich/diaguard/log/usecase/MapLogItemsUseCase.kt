package com.faltenreich.diaguard.log.usecase

import com.faltenreich.diaguard.log.item.LogItem
import com.faltenreich.diaguard.shared.datetime.MonthOfYear
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MapLogItemsUseCase(
    private val dispatcher: CoroutineDispatcher,
) {

    suspend operator fun invoke(
        data: List<LogItem>,
    ): Map<MonthOfYear, List<LogItem>> = withContext(dispatcher) {
        data.groupBy { it.date.year to it.date.monthOfYear }.map { (_, data) ->
            val date = data.first().date
            MonthOfYear(date.month, date.year) to data
        }.toMap()
    }
}