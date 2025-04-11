package com.faltenreich.diaguard.datetime.picker

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.datetime.Time
import com.faltenreich.diaguard.navigation.modal.Modal

data class TimePickerModal(
    private val time: Time,
    private val onPick: (Time) -> Unit,
) : Modal {

    @Composable
    override fun Content(modifier: Modifier) {
        TimePicker(time, onPick)
    }
}