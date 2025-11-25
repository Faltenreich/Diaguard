package com.faltenreich.diaguard.export.form

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
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
import com.faltenreich.diaguard.datetime.DateRangePickerDialog
import com.faltenreich.diaguard.export.ExportType
import com.faltenreich.diaguard.export.pdf.PdfLayout
import com.faltenreich.diaguard.measurement.category.icon.MeasurementCategoryIcon
import com.faltenreich.diaguard.view.checkbox.TextCheckbox
import com.faltenreich.diaguard.view.divider.Divider
import com.faltenreich.diaguard.view.divider.TextDivider
import com.faltenreich.diaguard.view.image.ResourceIcon
import com.faltenreich.diaguard.view.layout.FormRow
import com.faltenreich.diaguard.view.overlay.DropdownTextMenu
import diaguard.core.view.generated.resources.ic_note
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.data
import diaguard.shared.generated.resources.date_range_picker_open
import diaguard.shared.generated.resources.days_without_entries
import diaguard.shared.generated.resources.ic_document
import diaguard.shared.generated.resources.ic_skip
import diaguard.shared.generated.resources.ic_tag
import diaguard.shared.generated.resources.ic_time
import diaguard.shared.generated.resources.measurement_categories
import diaguard.shared.generated.resources.notes
import diaguard.shared.generated.resources.tags
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ExportForm(
    state: ExportFormState?,
    onIntent: (ExportFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    state ?: return

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        var showDateRangePicker by remember { mutableStateOf(false) }
        FormRow(
            icon = { ResourceIcon(Res.drawable.ic_time) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClickLabel = stringResource(Res.string.date_range_picker_open),
                    role = Role.Button,
                    onClick = { showDateRangePicker = true },
                ),
        ) {
            Text(
                text = state.date.dateRangeLocalized,
                modifier = Modifier.fillMaxWidth(),
            )
            if (showDateRangePicker) {
                DateRangePickerDialog(
                    dateRange = state.date.dateRange,
                    onDismissRequest = { showDateRangePicker = false },
                    onConfirmRequest = { dateRange ->
                        showDateRangePicker = false
                        onIntent(ExportFormIntent.SetDateRange(dateRange))
                    },
                )
            }
        }

        Divider()

        var expandDropdownForType by remember { mutableStateOf(false) }
        FormRow(
            icon = { ResourceIcon(Res.drawable.ic_document) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expandDropdownForType = true },
        ) {
            Text(stringResource(state.type.selection.title))

            DropdownTextMenu(
                expanded = expandDropdownForType,
                onDismissRequest = { expandDropdownForType = false },
                items = state.type.options.map { type ->
                    stringResource(type.title) to {
                        onIntent(ExportFormIntent.SelectType(type))
                    }
                },
            )
        }

        AnimatedVisibility(visible = state.type.selection == ExportType.PDF) {
            ExportPdfLayoutForm(
                state = state,
                onIntent = onIntent,
            )
        }

        TextDivider(stringResource(Res.string.data))

        FormRow(
            icon = { ResourceIcon(diaguard.core.view.generated.resources.Res.drawable.ic_note) },
            modifier = Modifier.toggleable(
                value = state.content.includeNotes,
                role = Role.Checkbox,
                onValueChange = { onIntent(ExportFormIntent.SetIncludeNotes(it)) },
            ),
        ) {
            TextCheckbox(
                title = stringResource(Res.string.notes),
                checked = state.content.includeNotes,
                onCheckedChange = null,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Divider()

        FormRow(
            icon = { ResourceIcon(Res.drawable.ic_tag) },
            modifier = Modifier.toggleable(
                value = state.content.includeTags,
                role = Role.Checkbox,
                onValueChange = { onIntent(ExportFormIntent.SetIncludeTags(it)) },
            ),
        ) {
            TextCheckbox(
                title = stringResource(Res.string.tags),
                checked = state.content.includeTags,
                onCheckedChange = null,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Divider()

        FormRow(
            icon = { ResourceIcon(Res.drawable.ic_skip) },
            modifier = Modifier.toggleable(
                value = state.layout.includeDaysWithoutEntries,
                role = Role.Checkbox,
                onValueChange = { onIntent(ExportFormIntent.SetIncludeDaysWithoutEntries(it)) },
            ),
        ) {
            TextCheckbox(
                title = stringResource(Res.string.days_without_entries),
                checked = state.layout.includeDaysWithoutEntries,
                onCheckedChange = null,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        TextDivider(stringResource(Res.string.measurement_categories))

        state.content.categories.forEach { category ->
            FormRow(
                icon = { MeasurementCategoryIcon(category.category) },
                modifier = Modifier.toggleable(
                    value = category.isExported,
                    role = Role.Checkbox,
                    onValueChange = {
                        onIntent(ExportFormIntent.SetCategory(category.copy(isExported = !category.isExported)))
                    },
                ),
            ) {
                TextCheckbox(
                    title = category.category.name,
                    checked = category.isExported,
                    onCheckedChange = null,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Divider()
        }
    }
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    ExportForm(
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