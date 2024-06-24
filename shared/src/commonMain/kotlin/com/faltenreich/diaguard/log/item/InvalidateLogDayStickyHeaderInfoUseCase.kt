package com.faltenreich.diaguard.log.item

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.faltenreich.diaguard.log.LogKey
import kotlin.math.min

class InvalidateLogDayStickyHeaderInfoUseCase {

    operator fun invoke(
        stickyHeaderInfo: LogDayStickyHeaderInfo,
        monthHeaderSize: IntSize,
        dayHeaderSize: IntSize,
        firstItem: LogItem,
        nextItems: List<LazyListItemInfo>,
    ): LogDayStickyHeaderInfo {
        if (firstItem is LogItem.MonthHeader) {
            return stickyHeaderInfo.copy(offset = IntOffset(x = 0, y = -dayHeaderSize.height))
        }

        val nextItem = nextItems.firstOrNull { item ->
            when (val key = item.key) {
                is LogKey.Header -> true
                is LogKey.Item -> key.isFirstOfDay && key.date > firstItem.date
                else -> true
            }
        }
        val offset = when (nextItem?.key) {
            is LogKey.Header -> -dayHeaderSize.height
            is LogKey.Item -> min(monthHeaderSize.height, nextItem.offset - dayHeaderSize.height)
            else -> -dayHeaderSize.height
        }

        val overlap = -(offset - monthHeaderSize.height)
        val clip = if (overlap > 0) overlap.toFloat() else 0f

        return stickyHeaderInfo.copy(
            date = firstItem.date,
            style = firstItem.style.copy(isVisible = true),
            offset = IntOffset(x = 0, y = offset),
            clip = clip,
        )
    }
}