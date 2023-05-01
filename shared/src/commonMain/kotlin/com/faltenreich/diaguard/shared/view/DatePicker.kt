package com.faltenreich.diaguard.shared.view

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.shared.datetime.DateTime

@Composable
expect fun DatePicker(
    dateTime: DateTime,
    onDatePick: (DateTime) -> Unit,
)