package com.faltenreich.rhyme.shared.view

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.rhyme.MR
import com.faltenreich.rhyme.shared.di.inject
import com.faltenreich.rhyme.shared.localization.Localization

@Composable
fun ClearButton(
    onClick: () -> Unit,
    localization: Localization = inject(),
) {
    Icon(
        Icons.Default.Clear,
        contentDescription = localization.getString(MR.strings.clear_input),
        modifier = Modifier.clickable { onClick() },
    )
}