package com.faltenreich.diaguard.export.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.faltenreich.diaguard.data.export.ExportType
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.ic_file
import com.faltenreich.diaguard.view.layout.FormRow
import com.faltenreich.diaguard.view.theme.AppTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ExportHistoryListItem(
    file: ExportFile,
    onIntent: (ExportHistoryIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    FormRow(
        icon = {
            Box(contentAlignment = Alignment.BottomCenter) {
                Icon(
                    painter = painterResource(Res.drawable.ic_file),
                    contentDescription = null,
                    modifier = Modifier.size(AppTheme.dimensions.size.ImageLarge),
                    tint = file.type.color,
                )
                Text(
                    text = stringResource(file.type.title),
                    modifier = Modifier.padding(bottom = AppTheme.dimensions.padding.P_1),
                    style = AppTheme.typography.labelSmall,
                )
            }
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