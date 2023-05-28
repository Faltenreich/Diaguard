package com.faltenreich.diaguard.log.usecase

import com.faltenreich.diaguard.log.item.LogData
import com.faltenreich.diaguard.shared.datetime.Month
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MapLogDataUseCase(
    private val dispatcher: CoroutineDispatcher,
) {

    suspend operator fun invoke(
        data: List<LogData>,
    ): Map<Month, List<LogData>> = withContext(dispatcher) {
        data.groupBy { it.date.year to it.date.monthOfYear }.map { (_, data) ->
            data.first().date.month to data
        }.toMap()
    }
}