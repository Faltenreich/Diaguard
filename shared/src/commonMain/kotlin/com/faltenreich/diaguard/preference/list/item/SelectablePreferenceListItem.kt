package com.faltenreich.diaguard.preference.list.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.preference.list.Preference
import com.faltenreich.diaguard.shared.view.DropDownMenu
import kotlinx.coroutines.launch

@Composable
fun <T : SelectablePreference> SelectablePreferenceItem(
    preference: Preference.Selection<T>,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    Box(modifier = modifier) {
        PreferenceListItem(
            preference = preference,
            modifier = Modifier.clickable { isExpanded = true },
        )
        DropDownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = modifier,
        ) {
            preference.options.forEach { option ->
                Text(option.label(), modifier = Modifier
                    .clickable {
                        scope.launch {
                            option.onSelected()
                            isExpanded = false
                        }
                    }
                    .fillMaxWidth()
                    .background(if (option.isSelected) AppTheme.colors.material.secondaryContainer else Color.Transparent)
                    .padding(all = AppTheme.dimensions.padding.P_3),
                )
            }
        }
    }
}