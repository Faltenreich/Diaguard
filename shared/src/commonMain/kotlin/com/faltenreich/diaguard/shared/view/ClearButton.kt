package com.faltenreich.diaguard.shared.view

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import diaguard.shared.generated.resources.*
import com.faltenreich.diaguard.shared.localization.getString
import org.jetbrains.compose.resources.painterResource

@Composable
fun ClearButton(
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_clear),
            contentDescription = getString(Res.string.clear_input),
        )
    }
}