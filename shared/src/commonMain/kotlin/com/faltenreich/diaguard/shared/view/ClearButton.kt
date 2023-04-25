package com.faltenreich.diaguard.shared.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization

@Composable
fun ClearButton(
    localization: Localization = inject(),
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
    ) {
        Icon(
            Icons.Default.Clear,
            contentDescription = localization.getString(com.faltenreich.diaguard.MR.strings.clear_input),
        )
    }
}