package com.faltenreich.diaguard.preference.list.item

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.icerock.moko.resources.StringResource

class PreferenceActionListItem(
    title: StringResource,
    subtitle: String?,
    private val action: () -> Unit,
) : PreferenceListItem(title, subtitle) {

    @Composable
    override fun Content(modifier: Modifier) {
        PreferenceListItemLayout(
            preference = this,
            modifier = modifier.clickable { action() },
        )
    }

    class Builder {

        lateinit var title: StringResource
        var subtitle: String? = null
        lateinit var onClick: () -> Unit

        fun build(): PreferenceActionListItem {
            return PreferenceActionListItem(title, subtitle, onClick)
        }
    }
}