package com.faltenreich.diaguard.export.history

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.faltenreich.diaguard.data.export.ExportType
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.view.layout.FormRow

@Composable
internal fun ExportHistoryListItem(
    file: ExportFile,
    onIntent: (ExportHistoryIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    FormRow(
        icon = {

        },
        modifier = modifier.clickable {
            onIntent(ExportHistoryIntent.OpenExport(file))
        },
    ) {
        Text(
            text = file.dateTime,
            modifier = Modifier.weight(1f),
        )
    }
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    ExportHistoryListItem(
        file = ExportFile(
            dateTime = now().toString(),
            type = ExportType.PDF,
        ),
        onIntent = {},
    )
}