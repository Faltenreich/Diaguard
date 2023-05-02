package com.faltenreich.diaguard.shared.view

import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTimeApi
import com.faltenreich.diaguard.shared.datetime.kotlinx.KotlinxTime
import com.faltenreich.diaguard.shared.di.inject
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.datetime.LocalTime

@Composable
actual fun DatePicker(
    date: Date,
    onPick: (Date) -> Unit,
) {
    val api = inject<DateTimeApi>()
    val state = rememberDatePickerState(
        // TODO: Remove dependency to library
        initialSelectedDateMillis = date.atTime(KotlinxTime(LocalTime(0, 0))).millisSince1970,
    )
    DatePickerDialog(
        onDismissRequest = { onPick(date) },
        confirmButton = {
            TextButton(onClick = { onPick(state.selectedDateMillis?.let(api::date) ?: date) }) {
                Text(stringResource(MR.strings.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = { onPick(date) }) {
                Text(stringResource(MR.strings.cancel))
            }
        },
    ) {
        androidx.compose.material3.DatePicker(state = state)
    }
}