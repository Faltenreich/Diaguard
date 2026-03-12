package com.faltenreich.diaguard.log.list.item

import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.faltenreich.diaguard.data.preview.PreviewScaffold

@Composable
fun LogLoadingIndicator(modifier: Modifier = Modifier) {
    LinearProgressIndicator(modifier = modifier)
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    LogLoadingIndicator()
}