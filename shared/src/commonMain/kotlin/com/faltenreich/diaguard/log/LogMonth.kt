package com.faltenreich.diaguard.log

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTimeFormatter
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun LogMonth(
    date: Date,
    modifier: Modifier = Modifier,
    formatter: DateTimeFormatter = inject(),
) {
    Box(
        modifier = modifier
            .aspectRatio(16f / 9f)
            .background(Color.LightGray)
            .padding(all = AppTheme.dimensions.padding.P_2),
    ) {
        Text(formatter.formatMonth(date, abbreviated = false))
    }
}