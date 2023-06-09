package com.faltenreich.diaguard.log.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
    modifier: Modifier = Modifier,
    formatter: DateTimeFormatter = inject(),
) {
    Column(
        modifier = modifier
            .padding(all = AppTheme.dimensions.padding.P_3),
    ) {
        // Text(formatter.formatDayOfMonth(date))
        Text(formatter.formatDate(date))
        Text(formatter.formatDayOfWeek(date, abbreviated = true))
    }
}