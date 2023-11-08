package com.faltenreich.diaguard.preference.list.item

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.icerock.moko.resources.StringResource

// TODO: Implement Serializable
sealed class PreferenceListItem(
    val title: StringResource,
    val subtitle: String?,
) {

    @Composable
    abstract fun Content(modifier: Modifier)
}