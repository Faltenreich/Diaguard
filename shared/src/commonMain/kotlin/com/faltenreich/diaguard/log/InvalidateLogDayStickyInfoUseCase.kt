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
        if (firstItem is LogItemState.MonthHeader) {
            return LogDayStickyInfo(
                date = firstItem.date,
                style = firstItem.style.copy(isVisible = true),
                offset = IntOffset(x = 0, y = -dayHeaderHeight),
                clip = 0f,
            )
        }

        val nextItem = nextItems.firstOrNull { item ->
            when (val key = item.key) {
                is LogKey.Header -> true
                is LogKey.Item -> key.isFirstOfDay &&
                    (firstItem is LogItemState.MonthHeader || key.date > firstItem.date)
                else -> true
            }
        }
        val offset = when (nextItem?.key) {
            null -> -dayHeaderHeight
            else -> min(0, nextItem.offset - dayHeaderHeight)
        }

        val overlap = -offset
        // TODO: Clip bottom if next item !is LogKey.Item
        val clip = if (overlap > 0) overlap.toFloat() else 0f

        return stickyHeaderInfo.copy(
            date = firstItem.date,
            style = firstItem.style.copy(isVisible = true),
            offset = IntOffset(x = 0, y = offset),
            clip = clip,
        )
    }
}