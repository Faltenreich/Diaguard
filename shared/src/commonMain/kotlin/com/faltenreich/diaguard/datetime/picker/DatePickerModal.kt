package com.faltenreich.diaguard.datetime.picker

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.navigation.modal.Modal

data class DatePickerModal(
    private val date: Date,
    private val onPick: (Date) -> Unit,
) : Modal {

    @Composable
    override fun Content(modifier: Modifier) {
        DatePicker(date, onPick)
    }
}