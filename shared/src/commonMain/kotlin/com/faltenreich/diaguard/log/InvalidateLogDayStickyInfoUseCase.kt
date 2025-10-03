package com.faltenreich.diaguard.log

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.ui.unit.IntOffset
import com.faltenreich.diaguard.log.list.LogKey
import com.faltenreich.diaguard.log.list.item.LogDayStickyInfo
import com.faltenreich.diaguard.log.list.item.LogItemState
import kotlin.math.min

class InvalidateLogDayStickyInfoUseCase {

    operator fun invoke(
        stickyHeaderInfo: LogDayStickyInfo,
        monthHeaderHeight: Int,
        dayHeaderHeight: Int,
        firstItem: LogItemState,
        nextItems: List<LazyListItemInfo>,
    ): LogDayStickyInfo {
        if (firstItem is LogItemState.MonthHeader) {
            return stickyHeaderInfo.copy(offset = IntOffset(x = 0, y = -dayHeaderHeight))
        }

        val nextItem = nextItems.firstOrNull { item ->
            when (val key = item.key) {
                is LogKey.Header -> true
                is LogKey.Item -> key.isFirstOfDay && key.date > firstItem.date
                else -> true
            }
        }
        val offset = when (nextItem?.key) {
            is LogKey.Header -> -dayHeaderHeight
            is LogKey.Item -> min(monthHeaderHeight, nextItem.offset - dayHeaderHeight)
            else -> monthHeaderHeight
        }

        val overlap = -(offset - monthHeaderHeight)
        val clip = if (overlap > 0) overlap.toFloat() else 0f

        return stickyHeaderInfo.copy(
            date = firstItem.date,
            style = firstItem.style.copy(isVisible = true),
            offset = IntOffset(x = 0, y = offset),
            clip = clip,
        )
    }
}