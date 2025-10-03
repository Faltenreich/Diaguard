package com.faltenreich.diaguard.log

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.ui.unit.IntOffset
import com.faltenreich.diaguard.log.list.LogKey
import com.faltenreich.diaguard.log.list.item.LogDayStickyInfo
import com.faltenreich.diaguard.log.list.item.LogItemState

class InvalidateLogDayStickyInfoUseCase {

    operator fun invoke(
        stickyHeaderInfo: LogDayStickyInfo,
        dayHeaderHeight: Int,
        firstItem: LogItemState,
        nextItems: List<LazyListItemInfo>,
    ): LogDayStickyInfo {
        val nextItem = nextItems.firstOrNull { item ->
            when (val key = item.key) {
                is LogKey.Header -> true
                is LogKey.Item -> key.isFirstOfDay && key.date > firstItem.date
                else -> true
            }
        }
        val offset = when (nextItem?.key) {
            is LogKey.Header -> -dayHeaderHeight
            is LogKey.Item -> nextItem.offset - dayHeaderHeight
            else -> -dayHeaderHeight
        }

        val overlap = -offset
        val clip = if (overlap > 0) overlap.toFloat() else 0f

        return stickyHeaderInfo.copy(
            date = firstItem.date,
            style = firstItem.style.copy(isVisible = true),
            offset = IntOffset(x = 0, y = offset),
            clip = clip,
        )
    }
}