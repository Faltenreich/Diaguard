package com.faltenreich.diaguard.log.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.log.LogIntent
import com.faltenreich.diaguard.shared.localization.getString

@Composable
fun LogEmpty(
    item: LogItem.EmptyContent,
    onIntent: (LogIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        LogDay(
            date = item.date,
            style = LogDayStyle.NORMAL,
            modifier = Modifier.width(AppTheme.dimensions.size.LogDayWidth),
        )
        Box(
            modifier = Modifier
                .clickable { onIntent(LogIntent.CreateEntry(item.date)) }
                .fillMaxWidth()
                .padding(all = AppTheme.dimensions.padding.P_3),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(getString(MR.strings.no_entries))
        }
    }
}