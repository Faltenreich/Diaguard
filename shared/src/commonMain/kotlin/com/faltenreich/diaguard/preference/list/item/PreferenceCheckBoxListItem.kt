package com.faltenreich.diaguard.preference.list.item

import androidx.compose.foundation.clickable
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

data class PreferenceCheckBoxListItem(
    override val title: String,
    override val subtitle: String?,
    private val isChecked: Boolean,
    private val onCheckedChange: suspend (isChecked: Boolean) -> Unit,
) : PreferenceListItem {

    @Composable
    override fun Content(modifier: Modifier) {
        val scope = rememberCoroutineScope()
        PreferenceListItemScaffold(
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

@Composable
fun PreferenceCheckBoxListItem2(
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
) {
    PreferenceListItemScaffold(
        title = title,
        subtitle = subtitle,
        modifier = modifier.clickable { onCheckedChange(!isChecked) },
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { onCheckedChange(!isChecked) },
        )
    }
}