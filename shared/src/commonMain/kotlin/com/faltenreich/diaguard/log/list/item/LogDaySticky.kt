package com.faltenreich.diaguard.log.list.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.clipRect
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.log.LogState

@Composable
fun LogDaySticky(
    state: LogState,
    modifier: Modifier = Modifier,
) = with (state.dayStickyInfo) {
    date?.let {
        LogDay(
            date = date,
            style = style,
            modifier = modifier
                .offset { offset }
                .drawWithContent {
                    clipRect(top = clip) {
                        this@drawWithContent.drawContent()
                    }
                }
                .background(AppTheme.colors.scheme.background)
                .padding(all = AppTheme.dimensions.padding.P_3)
        )
    }
}