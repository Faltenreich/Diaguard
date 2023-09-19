package com.faltenreich.diaguard.shared.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.localization.getString

@Composable
fun ClearButton(
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
    ) {
        Icon(
            Icons.Default.Clear,
            contentDescription = getString(MR.strings.clear_input),
        )
    }
}