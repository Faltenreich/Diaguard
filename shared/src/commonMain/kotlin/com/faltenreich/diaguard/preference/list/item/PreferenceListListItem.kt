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
import kotlinx.coroutines.launch

data class PreferenceListListItem(
    override val title: String,
    override val subtitle: String?,
    private val options: List<Option>,
) : PreferenceListItem {

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
                    option.label to { scope.launch { option.onSelected() } }
                }
            )
        }
    }

    data class Option(
        val label: String,
        val isSelected: Boolean,
        val onSelected: suspend () -> Unit,
    )

    class Builder {

        lateinit var title: String
        var subtitle: String? = null
        lateinit var options: List<Option>

        fun build(): PreferenceListListItem {
            return PreferenceListListItem(title, subtitle, options)
        }
    }
}