package com.faltenreich.diaguard.view.button

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.clear_input
import com.faltenreich.diaguard.resource.ic_clear
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ClearButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_clear),
            contentDescription = stringResource(Res.string.clear_input),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    ClearButton(onClick = {})
}