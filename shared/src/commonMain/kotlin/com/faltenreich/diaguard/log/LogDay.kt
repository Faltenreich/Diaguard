package com.faltenreich.diaguard.log

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        modifier = modifier,
    ) {
        Text(formatter.formatDayOfMonth(date))
        Text(formatter.formatDayOfWeek(date))
    }
}