package com.faltenreich.diaguard.log.item

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.ui.unit.IntOffset
import com.faltenreich.diaguard.log.LogKey
import kotlin.math.min

class InvalidateLogDayStickyHeaderInfoUseCase {

    operator fun invoke(
        stickyHeaderInfo: LogDayStickyHeaderInfo,
        monthHeaderHeight: Int,
        dayHeaderHeight: Int,
        firstItem: LogItem,
        nextItems: List<LazyListItemInfo>,
    ): LogDayStickyHeaderInfo {
        if (firstItem is LogItem.MonthHeader) {
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
            else -> -dayHeaderHeight
        }

        val date = firstItem.date
        val style = firstItem.style.takeIf { it != LogDayStyle.HIDDEN } ?: LogDayStyle.NORMAL
        val overlap = -(offset - monthHeaderHeight)
        val clip = if (overlap > 0) overlap.toFloat() else 0f

        return stickyHeaderInfo.copy(
            date = date,
            style = style,
            offset = IntOffset(x = 0, y = offset),
            clip = clip,
        )
    }
}