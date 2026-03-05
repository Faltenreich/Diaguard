package com.faltenreich.diaguard.export.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.faltenreich.diaguard.data.export.ExportType
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.persistence.file.File
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.export_history_item_delete
import com.faltenreich.diaguard.resource.export_history_item_open
import com.faltenreich.diaguard.resource.export_history_item_share
import com.faltenreich.diaguard.resource.export_history_menu_open
import com.faltenreich.diaguard.resource.ic_file
import com.faltenreich.diaguard.resource.ic_more_vertical
import com.faltenreich.diaguard.view.layout.FormRow
import com.faltenreich.diaguard.view.overlay.DropdownTextMenu
import com.faltenreich.diaguard.view.theme.AppTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ExportHistoryListItem(
    file: ExportFile,
    onIntent: (ExportHistoryIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showMenu by remember { mutableStateOf(false) }

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
            text = file.dateTime ?: "", // TODO
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = { showMenu = true }) {
            Icon(
                painter = painterResource(Res.drawable.ic_more_vertical),
                contentDescription = stringResource(Res.string.export_history_menu_open),
            )
            DropdownTextMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                items = listOf(
                    stringResource(Res.string.export_history_item_open) to {
                        onIntent(ExportHistoryIntent.OpenExport(file))
                    },
                    stringResource(Res.string.export_history_item_share) to {
                        onIntent(ExportHistoryIntent.ShareExport(file))
                    },
                    stringResource(Res.string.export_history_item_delete) to {
                        onIntent(ExportHistoryIntent.DeleteExport(file))
                    },
                ),
            )
        }
    }
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    val dateTime = now()
    ExportHistoryListItem(
        file = ExportFile(
            dateTime = dateTime.toString(),
            type = ExportType.PDF,
            file = File(
                absolutePath = "",
                createdAt = dateTime,
            ),
        ),
        onIntent = {},
    )
}