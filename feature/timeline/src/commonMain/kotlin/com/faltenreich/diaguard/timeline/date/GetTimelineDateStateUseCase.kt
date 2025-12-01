package com.faltenreich.diaguard.timeline.date

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter

class GetTimelineDateStateUseCase(
    private val dateTimeFormatter: DateTimeFormatter,
) {

    operator fun invoke(
        initialDate: Date,
        currentDate: Date,
        datePickerDialog: TimelineDateState.DatePickerDialog?,
    ): TimelineDateState {
        val dayOfWeek = dateTimeFormatter.formatDayOfWeek(currentDate, abbreviated = false)
        val date = dateTimeFormatter.formatDate(currentDate)
        return TimelineDateState(
            initialDate = initialDate,
            currentDate = currentDate,
            currentDateLocalized = "$dayOfWeek, $date",
            datePickerDialog = datePickerDialog,
        )
    }
}