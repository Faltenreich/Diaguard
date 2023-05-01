package com.faltenreich.diaguard.shared.view

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.shared.datetime.Time

@Composable
expect fun TimePicker(
    time: Time,
    onPick: (Time) -> Unit,
)