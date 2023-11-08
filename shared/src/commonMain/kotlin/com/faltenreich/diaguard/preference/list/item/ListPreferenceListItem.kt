package com.faltenreich.diaguard.preference.list.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.preference.list.Preference
import com.faltenreich.diaguard.shared.view.DropdownTextMenu
import com.faltenreich.diaguard.shared.view.DropdownTextMenuItem
import kotlinx.coroutines.launch

@Composable
fun ListPreferenceItem(
    preference: Preference.List,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    Box(modifier = modifier) {
        PreferenceListItemLayout(
            preference = preference,
            modifier = Modifier.clickable { isExpanded = true },
        )
        DropdownTextMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            items = preference.options.map { option ->
                DropdownTextMenuItem(
                    label = option.label(),
                    onClick = { scope.launch { option.onSelected() } },
                    isSelected = { option.isSelected },
                )
            }
        )
    }
}