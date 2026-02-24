package com.faltenreich.diaguard.export.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.calendar_week
import com.faltenreich.diaguard.resource.date_of_export
import com.faltenreich.diaguard.resource.ic_layout
import com.faltenreich.diaguard.resource.ic_position_bottom_left
import com.faltenreich.diaguard.resource.ic_position_bottom_right
import com.faltenreich.diaguard.resource.ic_position_top_left
import com.faltenreich.diaguard.resource.layout
import com.faltenreich.diaguard.resource.page_number
import com.faltenreich.diaguard.view.checkbox.TextCheckbox
import com.faltenreich.diaguard.view.divider.Divider
import com.faltenreich.diaguard.view.divider.TextDivider
import com.faltenreich.diaguard.view.image.ResourceIcon
import com.faltenreich.diaguard.view.layout.FormRow
import com.faltenreich.diaguard.view.overlay.DropdownTextMenu
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ExportPdfLayoutForm(
    state: ExportFormState,
    onIntent: (ExportFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) = with(state) {
    Column(modifier = modifier) {
        TextDivider(stringResource(Res.string.layout))

        var expandDropdownForPdfLayout by remember { mutableStateOf(false) }
        FormRow(
            icon = { ResourceIcon(Res.drawable.ic_layout) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expandDropdownForPdfLayout = true },
        ) {
            Text(stringResource(settings.layout.selection.title))

            DropdownTextMenu(
                expanded = expandDropdownForPdfLayout,
                onDismissRequest = { expandDropdownForPdfLayout = false },
                items = settings.layout.options.map { layout ->
                    stringResource(layout.title) to {
                        val update = settings.copy(layout = settings.layout.copy(selection = layout))
                        onIntent(ExportFormIntent.SetSettings(update))
                    }
                },
            )
        }

        Divider()

        FormRow(
            icon = { ResourceIcon(Res.drawable.ic_position_top_left) },
            modifier = Modifier.toggleable(
                value = settings.date.includeCalendarWeek,
                role = Role.Checkbox,
                onValueChange = {
                    val update = settings.copy(date = settings.date.copy(includeCalendarWeek = it))
                    onIntent(ExportFormIntent.SetSettings(update))
                },
            ),
        ) {
            TextCheckbox(
                title = stringResource(Res.string.calendar_week),
                checked = settings.date.includeCalendarWeek,
                onCheckedChange = null,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Divider()

        FormRow(
            icon = { ResourceIcon(Res.drawable.ic_position_bottom_left) },
            modifier = Modifier.toggleable(
                value = settings.date.includeDateOfExport,
                role = Role.Checkbox,
                onValueChange = {
                    val update = settings.copy(date = settings.date.copy(includeDateOfExport = it))
                    onIntent(ExportFormIntent.SetSettings(update))
                },
            ),
        ) {
            TextCheckbox(
                title = stringResource(Res.string.date_of_export),
                checked = settings.date.includeDateOfExport,
                onCheckedChange = null,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Divider()

        FormRow(
            icon = { ResourceIcon(Res.drawable.ic_position_bottom_right) },
            modifier = Modifier.toggleable(
                value = settings.layout.includePageNumber,
                role = Role.Checkbox,
                onValueChange = {
                    val update = settings.copy(layout = settings.layout.copy(includePageNumber = it))
                    onIntent(ExportFormIntent.SetSettings(update))
                },
            ),
        ) {
            TextCheckbox(
                title = stringResource(Res.string.page_number),
                checked = settings.layout.includePageNumber,
                onCheckedChange = null,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
private fun Preview(
    @PreviewParameter(ExportFormState.Preview::class)
    state: ExportFormState,
) = PreviewScaffold {
    ExportPdfLayoutForm(
        state = state,
        onIntent = {},
    )
}