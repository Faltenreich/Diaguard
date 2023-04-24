package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization

@Composable
fun ClearButton(
    localization: Localization = inject(),
    onClick: () -> Unit,
) {
    Icon(
        Icons.Default.Clear,
        contentDescription = localization.getString(com.faltenreich.diaguard.MR.strings.clear_input),
        modifier = Modifier.clickable { onClick() },
    )
}