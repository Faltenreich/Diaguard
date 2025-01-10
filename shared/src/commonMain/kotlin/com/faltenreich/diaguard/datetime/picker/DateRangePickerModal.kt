package com.faltenreich.diaguard.datetime.picker

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.navigation.modal.Modal

data class DateRangePickerModal(
    private val dateRange: ClosedRange<Date>,
    private val onPick: (ClosedRange<Date>) -> Unit,
) : Modal {

    @Composable
    override fun Content() {
        DateRangePicker(dateRange, onPick)
    }
}