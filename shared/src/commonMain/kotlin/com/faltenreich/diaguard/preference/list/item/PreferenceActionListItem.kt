package com.faltenreich.diaguard.preference.list.item

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

class PreferenceActionListItem(
    title: String,
    subtitle: String?,
    private val action: suspend () -> Unit,
) : PreferenceListItem(title, subtitle) {

    @Composable
    override fun Content(modifier: Modifier) {
        val scope = rememberCoroutineScope()
        PreferenceListItemLayout(
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