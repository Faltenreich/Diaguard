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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.faltenreich.diaguard.data.export.ExportType
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.datetime.DateRangePickerDialog
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.data
import com.faltenreich.diaguard.resource.date_range_picker_open
import com.faltenreich.diaguard.resource.days_without_entries
import com.faltenreich.diaguard.resource.ic_document
import com.faltenreich.diaguard.resource.ic_note
import com.faltenreich.diaguard.resource.ic_skip
import com.faltenreich.diaguard.resource.ic_tag
import com.faltenreich.diaguard.resource.ic_time
import com.faltenreich.diaguard.resource.measurement_categories
import com.faltenreich.diaguard.resource.notes
import com.faltenreich.diaguard.resource.tags
import com.faltenreich.diaguard.view.checkbox.TextCheckbox
import com.faltenreich.diaguard.view.divider.Divider
import com.faltenreich.diaguard.view.divider.TextDivider
import com.faltenreich.diaguard.view.image.ResourceIcon
import com.faltenreich.diaguard.view.layout.FormRow
import com.faltenreich.diaguard.view.overlay.DropdownTextMenu
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ExportForm(
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
                text = state.dateRangeLocalized,
                modifier = Modifier.fillMaxWidth(),
            )
            if (showDateRangePicker) {
                DateRangePickerDialog(
                    dateRange = state.dateRange,
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
            Text(stringResource(state.settings.type.selection.title))

            DropdownTextMenu(
                expanded = expandDropdownForType,
                onDismissRequest = { expandDropdownForType = false },
                items = state.settings.type.options.map { type ->
                    stringResource(type.title) to {
                        val update = state.settings.copy(type = state.settings.type.copy(selection = type))
                        onIntent(ExportFormIntent.SetSettings(update))
                    }
                },
            )
        }

        AnimatedVisibility(visible = state.settings.type.selection == ExportType.PDF) {
            ExportPdfLayoutForm(
                state = state,
                onIntent = onIntent,
            )
        }

        TextDivider(stringResource(Res.string.data))

        FormRow(
            icon = { ResourceIcon(Res.drawable.ic_note) },
            modifier = Modifier.toggleable(
                value = state.settings.content.includeNotes,
                role = Role.Checkbox,
                onValueChange = {
                    val update = state.settings.copy(content = state.settings.content.copy(includeNotes = it))
                    onIntent(ExportFormIntent.SetSettings(update))
                },
            ),
        ) {
            TextCheckbox(
                title = stringResource(Res.string.notes),
                checked = state.settings.content.includeNotes,
                onCheckedChange = null,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Divider()

        FormRow(
            icon = { ResourceIcon(Res.drawable.ic_tag) },
            modifier = Modifier.toggleable(
                value = state.settings.content.includeTags,
                role = Role.Checkbox,
                onValueChange = {
                    val update = state.settings.copy(content = state.settings.content.copy(includeTags = it))
                    onIntent(ExportFormIntent.SetSettings(update))
                },
            ),
        ) {
            TextCheckbox(
                title = stringResource(Res.string.tags),
                checked = state.settings.content.includeTags,
                onCheckedChange = null,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Divider()

        FormRow(
            icon = { ResourceIcon(Res.drawable.ic_skip) },
            modifier = Modifier.toggleable(
                value = state.settings.layout.includeDaysWithoutEntries,
                role = Role.Checkbox,
                onValueChange = {
                    val update = state.settings.copy(
                        layout = state.settings.layout.copy(includeDaysWithoutEntries = it),
                    )
                    onIntent(ExportFormIntent.SetSettings(update))
                },
            ),
        ) {
            TextCheckbox(
                title = stringResource(Res.string.days_without_entries),
                checked = state.settings.layout.includeDaysWithoutEntries,
                onCheckedChange = null,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        TextDivider(stringResource(Res.string.measurement_categories))

        state.settings.content.categories.forEach { category ->
            ExportFormCategoryListItem(
                category = category,
                onIntent = onIntent,
            )
            Divider()
        }
    }
}

@Preview
@Composable
private fun Preview(
    @PreviewParameter(ExportFormState.Preview::class)
    state: ExportFormState,
) = PreviewScaffold {
    ExportForm(
        state = state,
        onIntent = {},
    )
}