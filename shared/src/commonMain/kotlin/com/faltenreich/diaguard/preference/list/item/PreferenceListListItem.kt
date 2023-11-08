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
import com.faltenreich.diaguard.shared.view.DropdownTextMenu
import com.faltenreich.diaguard.shared.view.DropdownTextMenuItem
import dev.icerock.moko.resources.StringResource
import kotlinx.coroutines.launch

class PreferenceListListItem(
    title: StringResource,
    subtitle: String?,
    private val options: List<Option>,
) : PreferenceListItem(title, subtitle) {

    @Composable
    override fun Content(modifier: Modifier) {
        val scope = rememberCoroutineScope()
        var isExpanded by rememberSaveable { mutableStateOf(false) }
        Box(modifier = modifier) {
            PreferenceListItemLayout(
                preference = this@PreferenceListListItem,
                modifier = Modifier.clickable { isExpanded = true },
            )
            DropdownTextMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                items = options.map { option ->
                    DropdownTextMenuItem(
                        label = option.label(),
                        onClick = { scope.launch { option.onSelected() } },
                        isSelected = { option.isSelected },
                    )
                }
            )
        }
    }

    data class Option(
        val label: @Composable () -> String,
        val isSelected: Boolean,
        val onSelected: suspend () -> Unit,
    )
}