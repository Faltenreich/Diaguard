package com.faltenreich.diaguard.datetime

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.injection.inject
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import diaguard.feature.datetime.generated.resources.Res
import diaguard.feature.datetime.generated.resources.cancel
import diaguard.feature.datetime.generated.resources.ok
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

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
    DatePickerDialog(
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
                Text(stringResource(Res.string.ok))
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(Res.string.cancel))
            }
        },
    ) {
        DatePicker(state = state)
    }
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    DatePickerDialog(
        date = today(),
        onDismissRequest = {},
        onConfirmRequest = {},
    )
}