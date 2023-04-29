package com.faltenreich.diaguard.shared.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.MR
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun ClearButton(
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
    ) {
        Icon(
            Icons.Default.Clear,
            contentDescription = stringResource(MR.strings.clear_input),
        )
    }
}