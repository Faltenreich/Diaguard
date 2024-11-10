package com.faltenreich.diaguard.preference.list.item

import androidx.compose.foundation.clickable
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

class PreferenceCheckBoxListItem(
    title: String,
    subtitle: String?,
    private val isChecked: Boolean,
    private val onCheckedChange: suspend (isChecked: Boolean) -> Unit,
) : PreferenceListItem(title, subtitle) {

    @Composable
    override fun Content(modifier: Modifier) {
        val scope = rememberCoroutineScope()
        PreferenceListItemLayout(
            preference = this,
            modifier = modifier.clickable { scope.launch { onCheckedChange(!isChecked) } },
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { scope.launch { onCheckedChange(!isChecked) } },
            )
        }
    }

    class Builder {

        lateinit var title: String
        var subtitle: String? = null
        var isChecked: Boolean = false
        lateinit var onCheckedChange: suspend (isChecked: Boolean) -> Unit

        fun build(): PreferenceCheckBoxListItem {
            return PreferenceCheckBoxListItem(title, subtitle, isChecked, onCheckedChange)
        }
    }
}