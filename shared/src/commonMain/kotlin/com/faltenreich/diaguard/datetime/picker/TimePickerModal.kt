package com.faltenreich.diaguard.datetime.picker

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.datetime.Time
import com.faltenreich.diaguard.navigation.Modal

data class TimePickerModal(
    private val time: Time,
    private val onPick: (Time) -> Unit,
) : Modal {

    @Composable
    override fun Content() {
        TimePicker(time, onPick)
    }
}