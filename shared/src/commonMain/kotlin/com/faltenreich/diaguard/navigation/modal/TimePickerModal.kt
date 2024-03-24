package com.faltenreich.diaguard.navigation.modal

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.datetime.Time
import com.faltenreich.diaguard.datetime.picker.TimePicker

data class TimePickerModal(
    private val time: Time,
    private val onPick: (Time) -> Unit,
) : Modal {

    @Composable
    override fun Content() {
        TimePicker(time, onPick)
    }
}