package com.faltenreich.diaguard.shared.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarItem
import com.faltenreich.diaguard.shared.datetime.Date

@Composable
fun DatePickerBottomAppBarItem(
    date: Date,
    onDatePick: (Date) -> Unit,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    BottomAppBarItem(
        image = Icons.Filled.DateRange,
        contentDescription = MR.strings.date_pick,
        onClick = { showDatePicker = true },
    )
    if (showDatePicker) {
        DatePicker(
            date = date,
            onPick = {
                showDatePicker = false
                onDatePick(it)
            },
        )
    }
}