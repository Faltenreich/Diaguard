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
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.datetime.DateRange
import com.faltenreich.diaguard.export.ExportType
import com.faltenreich.diaguard.export.pdf.PdfLayout
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
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ExportPdfLayoutForm(
    state: ExportFormState,
    onIntent: (ExportFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        TextDivider(stringResource(Res.string.layout))

        var expandDropdownForPdfLayout by remember { mutableStateOf(false) }
        FormRow(
            icon = { ResourceIcon(Res.drawable.ic_layout) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expandDropdownForPdfLayout = true },
        ) {
            Text(stringResource(state.layout.selection.title))

            DropdownTextMenu(
                expanded = expandDropdownForPdfLayout,
                onDismissRequest = { expandDropdownForPdfLayout = false },
                items = state.layout.options.map { layout ->
                    stringResource(layout.title) to { onIntent(ExportFormIntent.SelectLayout(layout)) }
                },
            )
        }

        Divider()

        FormRow(
            icon = { ResourceIcon(Res.drawable.ic_position_top_left) },
            modifier = Modifier.toggleable(
                value = state.date.includeCalendarWeek,
                role = Role.Checkbox,
                onValueChange = { onIntent(ExportFormIntent.SetIncludeCalendarWeek(it)) },
            ),
        ) {
            TextCheckbox(
                title = stringResource(Res.string.calendar_week),
                checked = state.date.includeCalendarWeek,
                onCheckedChange = null,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Divider()

        FormRow(
            icon = { ResourceIcon(Res.drawable.ic_position_bottom_left) },
            modifier = Modifier.toggleable(
                value = state.date.includeDateOfExport,
                role = Role.Checkbox,
                onValueChange = { onIntent(ExportFormIntent.SetIncludeDateOfExport(it)) },
            ),
        ) {
            TextCheckbox(
                title = stringResource(Res.string.date_of_export),
                checked = state.date.includeDateOfExport,
                onCheckedChange = null,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Divider()

        FormRow(
            icon = { ResourceIcon(Res.drawable.ic_position_bottom_right) },
            modifier = Modifier.toggleable(
                value = state.layout.includePageNumber,
                role = Role.Checkbox,
                onValueChange = { onIntent(ExportFormIntent.SetIncludePageNumber(it)) },
            ),
        ) {
            TextCheckbox(
                title = stringResource(Res.string.page_number),
                checked = state.layout.includePageNumber,
                onCheckedChange = null,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    ExportPdfLayoutForm(
        state = ExportFormState(
            date = ExportFormState.Date(
                dateRange = today().let { today -> DateRange(today, today) },
                dateRangeLocalized = "DateRange",
                includeCalendarWeek = true,
                includeDateOfExport = true,
            ),
            type = ExportFormState.Type(
                selection = ExportType.PDF,
                options = emptyList(),
            ),
            layout = ExportFormState.Layout(
                selection = PdfLayout.TIMELINE,
                options = emptyList(),
                includePageNumber = true,
                includeDaysWithoutEntries = true,
            ),
            content = ExportFormState.Content(
                categories = listOf(
                    ExportFormState.Content.Category(
                        category = category(),
                        isExported = true,
                    ),
                ),
                includeNotes = true,
                includeTags = true,
            ),
        ),
        onIntent = {},
    )
}