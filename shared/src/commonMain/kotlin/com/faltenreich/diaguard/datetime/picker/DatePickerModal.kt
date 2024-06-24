package com.faltenreich.diaguard.datetime.picker

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.navigation.Modal

data class DatePickerModal(
    private val date: Date,
    private val onPick: (Date) -> Unit,
) : Modal {

    @Composable
    override fun Content() {
        DatePicker(date, onPick)
    }
}