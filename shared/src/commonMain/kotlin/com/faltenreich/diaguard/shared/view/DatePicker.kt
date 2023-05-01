package com.faltenreich.diaguard.shared.view

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.shared.datetime.Date

@Composable
expect fun DatePicker(
    date: Date,
    onPick: (Date) -> Unit,
)