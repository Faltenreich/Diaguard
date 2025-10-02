package com.faltenreich.diaguard.log

import androidx.compose.ui.unit.IntSize
import androidx.paging.PagingData
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.log.list.item.LogDayStickyInfo
import com.faltenreich.diaguard.log.list.item.LogItemState
import kotlinx.coroutines.flow.Flow

data class LogState(
    val pagingData: Flow<PagingData<LogItemState>>,
    val dayHeaderSize: IntSize,
    val dayStickyInfo: LogDayStickyInfo,
    val datePickerDialog: DatePickerDialog?,
) {

    data class DatePickerDialog(val date: Date)
}