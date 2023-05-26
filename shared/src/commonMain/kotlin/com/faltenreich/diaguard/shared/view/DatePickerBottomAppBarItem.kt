package com.faltenreich.diaguard.shared.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarItem
import com.faltenreich.diaguard.shared.datetime.Date

@Composable
fun DatePickerBottomAppBarItem(
    date: Date,
    onDatePick: (Date) -> Unit,
) {
    val datePickerState = rememberDatePickerState()
    BottomAppBarItem(
        image = Icons.Filled.DateRange,
        contentDescription = MR.strings.date_pick,
        onClick = { datePickerState.isShown = true },
    )
    if (datePickerState.isShown) {
        DatePicker(
            date = date,
            onPick = {
                datePickerState.isShown = false
                onDatePick(it)
            },
        )
    }
}