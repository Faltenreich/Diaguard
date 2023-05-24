package com.faltenreich.diaguard.log

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.MR
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun LogEmpty(
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(MR.strings.no_entries),
        modifier = modifier,
    )
}