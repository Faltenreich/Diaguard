package com.faltenreich.diaguard.preference.list.item

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

data class PreferenceActionListItem(
    override val title: String,
    override val subtitle: String?,
    private val action: suspend () -> Unit,
) : PreferenceListItem {

    @Composable
    override fun Content(modifier: Modifier) {
        val scope = rememberCoroutineScope()
        PreferenceListItemScaffold(
            preference = this,
            modifier = modifier.clickable { scope.launch { action() } },
        )
    }

    class Builder {

        lateinit var title: String
        var subtitle: String? = null
        lateinit var onClick: suspend () -> Unit

        fun build(): PreferenceActionListItem {
            return PreferenceActionListItem(title, subtitle, onClick)
        }
    }
}

@Composable
fun PreferenceActionListItem(
    title: String,
    onClick: () -> Unit,
    subtitle: String? = null,
) {
    PreferenceListItemScaffold(
        title = title ,
        subtitle = subtitle,
        modifier = Modifier.clickable { onClick() },
    )
}