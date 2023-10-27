package com.faltenreich.diaguard.entry.form.alarm

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.DropdownTextMenu
import com.faltenreich.diaguard.shared.view.DropdownTextMenuItem
import com.faltenreich.diaguard.shared.view.TextInput

@Composable
fun AlarmPicker(
    delay: AlarmDelay,
    onPick: (AlarmDelay) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    var showCustomNumberPicker by remember { mutableStateOf(false) }
    var customNumber by mutableStateOf<Int?>(null)

    TextButton(onClick = { expanded = true }) {
        Text(delay.minutes?.let { minutes -> getString(MR.strings.alarm_placeholder, minutes) }
            ?: getString(MR.strings.alarm_none))
    }
    DropdownTextMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        items = AlarmDelay.entries.map { other ->
            DropdownTextMenuItem(
                label = getString(other.label),
                onClick = {
                    when (other) {
                        is AlarmDelay.Custom -> showCustomNumberPicker = true
                        else -> onPick(other)
                    }
                },
                isSelected = { other == delay },
            )
        },
        modifier = modifier,
    )

    if (showCustomNumberPicker) {
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) { focusRequester.requestFocus() }
        AlertDialog(
            onDismissRequest = { showCustomNumberPicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val number = customNumber
                        onPick(AlarmDelay.Custom(number))
                        showCustomNumberPicker = false
                    }
                ) {
                    Text(getString(MR.strings.ok))
                }
            },
            modifier = modifier,
            dismissButton = {
                TextButton(onClick = { showCustomNumberPicker = false }) {
                    Text(getString(MR.strings.cancel))
                }
            },
            title = { Text(getString(MR.strings.measurement_type_new)) },
            text = {
                TextInput(
                    input = customNumber?.toString() ?: "",
                    onInputChange = { customNumber = it.toIntOrNull() },
                    modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                    label = getString(MR.strings.alarm_custom),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
            }
        )
    }
}