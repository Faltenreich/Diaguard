package com.faltenreich.diaguard.log.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTimeFormatter
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun LogDay(
    date: Date,
    style: LogDayStyle,
    modifier: Modifier = Modifier,
    formatter: DateTimeFormatter = inject(),
) {
    if (style == LogDayStyle.HIDDEN) {
        Box(modifier = modifier)
    } else {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_1),
        ) {
            Text(
                text = formatter.formatDayOfMonth(date),
                color =
                if (style == LogDayStyle.HIGHLIGHTED) AppTheme.colors.scheme.primary
                else AppTheme.colors.scheme.onBackground,
                style = AppTheme.typography.headlineSmall,
            )
            Text(
                text = formatter.formatDayOfWeek(date, abbreviated = true),
                style = AppTheme.typography.labelMedium,
            )
        }
    }
}