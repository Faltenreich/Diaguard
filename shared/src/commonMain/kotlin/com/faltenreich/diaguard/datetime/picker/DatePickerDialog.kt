package com.faltenreich.diaguard.datetime.picker

import androidx.compose.material3.DatePicker
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.cancel
import diaguard.shared.generated.resources.ok

@Composable
fun DatePickerDialog(
    date: Date,
    onDismissRequest: () -> Unit,
    onConfirmRequest: (Date) -> Unit,
    modifier: Modifier = Modifier,
    dateTimeFactory: DateTimeFactory = inject<DateTimeFactory>(),
) {
    val state = rememberDatePickerState(
        initialSelectedDateMillis = date.atStartOfDay().epochMilliseconds,
    )
    androidx.compose.material3.DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    when (val update = state.selectedDateMillis?.let(dateTimeFactory::dateTimeFromEpoch)?.date) {
                        null -> onDismissRequest()
                        else -> onConfirmRequest(update)
                    }
                },
            ) {
                Text(getString(Res.string.ok))
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(getString(Res.string.cancel))
            }
        },
    ) {
        DatePicker(state = state)
    }
}