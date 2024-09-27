package com.faltenreich.diaguard.log.item

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.datetime.list.LogDay
import diaguard.shared.generated.resources.*
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
            style = item.style,
            modifier = Modifier.width(AppTheme.dimensions.size.LogDayWidth),
        )
        Card(
            onClick = { onIntent(LogIntent.CreateEntry(item.date)) },
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(containerColor = AppTheme.colors.Transparent),
        ) {
            Text(
                text = getString(Res.string.no_entries),
                color = AppTheme.colors.scheme.outline,
                modifier = Modifier.padding(all = AppTheme.dimensions.padding.P_3),
            )
        }
    }
}