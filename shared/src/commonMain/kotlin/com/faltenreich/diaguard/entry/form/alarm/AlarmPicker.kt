package com.faltenreich.diaguard.entry.form.alarm

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.DropdownTextMenu
import com.faltenreich.diaguard.shared.view.DropdownTextMenuItem

@Composable
fun AlarmPicker(
    minutes: Int?,
    onPick: (AlarmDelay) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    TextButton(onClick = { expanded = true }) {
        Text(getString(MR.strings.alarm_placeholder))
    }
    DropdownTextMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        items = AlarmDelay.entries.map { delay ->
            DropdownTextMenuItem(
                label = getString(delay.label),
                onClick = { onPick(delay) },
                isSelected = {
                    when (delay) {
                        AlarmDelay.NONE -> minutes == null
                        AlarmDelay.CUSTOM -> false // TODO
                        else -> minutes == delay.minutes
                    }
                }
            )
        },
        modifier = modifier,
    )
}