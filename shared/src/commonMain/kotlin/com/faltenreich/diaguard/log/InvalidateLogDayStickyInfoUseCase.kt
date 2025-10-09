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
        dayHeaderHeight: Int,
        firstItem: LogItemState,
        nextItems: List<LazyListItemInfo>,
    ): LogDayStickyInfo {
        val dayState = firstItem.dayState.copy(style = firstItem.dayState.style.copy(isVisible = true))

        if (firstItem is LogItemState.MonthHeader) {
            return LogDayStickyInfo(
                dayState = dayState,
                offset = IntOffset(x = 0, y = -dayHeaderHeight),
            )
        }

        val nextItem = nextItems.firstOrNull { item ->
            when (val key = item.key) {
                is LogKey.Header -> true
                is LogKey.Item -> key.isFirstOfDay &&
                    (firstItem is LogItemState.MonthHeader || key.date > firstItem.dayState.date)
                else -> true
            }
        }
        // FIXME: Overlaps next item if LogKey.Header when scrolling fast
        val offset = when (nextItem?.key) {
            is LogKey.Header,
            is LogKey.Item -> min(0, nextItem.offset - dayHeaderHeight)
            else -> 0
        }

        return stickyHeaderInfo.copy(
            dayState = dayState,
            offset = IntOffset(x = 0, y = offset),
        )
    }
}