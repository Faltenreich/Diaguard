package com.faltenreich.diaguard.navigation.modal

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.shared.datetime.Time
import com.faltenreich.diaguard.shared.view.TimePicker

data class TimePickerModal(
    private val time: Time,
    private val onPick: (Time) -> Unit,
) : Modal {

    @Composable
    override fun Content() {
        TimePicker(
            time = time,
            onPick = onPick,
        )
    }
}