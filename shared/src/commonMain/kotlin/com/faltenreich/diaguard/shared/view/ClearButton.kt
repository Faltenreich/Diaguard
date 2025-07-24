package com.faltenreich.diaguard.shared.view

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.clear_input
import diaguard.shared.generated.resources.ic_clear
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ClearButton(
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(Res.drawable.ic_clear),
            contentDescription = getString(Res.string.clear_input),
        )
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    ClearButton(onClick = {})
}