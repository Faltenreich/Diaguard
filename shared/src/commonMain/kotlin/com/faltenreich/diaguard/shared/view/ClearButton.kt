package com.faltenreich.diaguard.shared.view

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.localization.getString
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun ClearButton(
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
    ) {
        Icon(
            painter = painterResource(MR.images.ic_clear),
            contentDescription = getString(MR.strings.clear_input),
        )
    }
}