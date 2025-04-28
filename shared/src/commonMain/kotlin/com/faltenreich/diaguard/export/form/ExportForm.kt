package com.faltenreich.diaguard.export.form

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
import com.faltenreich.diaguard.datetime.picker.DateRangePicker
import com.faltenreich.diaguard.measurement.category.icon.MeasurementCategoryIcon
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.DropdownTextMenu
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.ResourceIcon
import com.faltenreich.diaguard.shared.view.TextCheckbox
import com.faltenreich.diaguard.shared.view.TextDivider
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.calendar_week
import diaguard.shared.generated.resources.data
import diaguard.shared.generated.resources.date_of_export
import diaguard.shared.generated.resources.days_without_entries
import diaguard.shared.generated.resources.ic_document
import diaguard.shared.generated.resources.ic_layout
import diaguard.shared.generated.resources.ic_note
import diaguard.shared.generated.resources.ic_position_bottom_left
import diaguard.shared.generated.resources.ic_position_bottom_right
import diaguard.shared.generated.resources.ic_position_top_left
import diaguard.shared.generated.resources.ic_skip
import diaguard.shared.generated.resources.ic_tag
import diaguard.shared.generated.resources.ic_time
import diaguard.shared.generated.resources.layout
import diaguard.shared.generated.resources.measurement_categories
import diaguard.shared.generated.resources.notes
import diaguard.shared.generated.resources.page_number
import diaguard.shared.generated.resources.tags
import org.jetbrains.compose.resources.stringResource

@Composable
fun ExportForm(
    viewModel: ExportFormViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.collectState() ?: return
    var showDateRangePicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        FormRow(
            icon = { ResourceIcon(Res.drawable.ic_time) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDateRangePicker = true },
        ) {
            Text(state.date.dateRangeLocalized)

            if (showDateRangePicker) {
                DateRangePicker(
                    dateRange = viewModel.dateRange.value,
                    onPick = {
                        showDateRangePicker = false
                        viewModel.dateRange.value = it
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
                    getString(type.title) to {
                        viewModel.dispatchIntent(ExportFormIntent.SelectType(type))
                    }
                },
            )
        }

        TextDivider(getString(Res.string.layout))

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
                    getString(layout.title) to {
                        viewModel.dispatchIntent(ExportFormIntent.SelectLayout(layout))
                    }
                },
            )
        }

        Divider()

        FormRow(
            icon = { ResourceIcon(Res.drawable.ic_position_top_left) },
            modifier = Modifier.toggleable(
                value = state.date.includeCalendarWeek,
                role = Role.Checkbox,
                onValueChange = { viewModel.dispatchIntent(ExportFormIntent.SetIncludeCalendarWeek(it)) },
            ),
        ) {
            TextCheckbox(
                title = getString(Res.string.calendar_week),
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
                onValueChange = { viewModel.dispatchIntent(ExportFormIntent.SetIncludeDateOfExport(it)) },
            ),
        ) {
            TextCheckbox(
                title = getString(Res.string.date_of_export),
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
                onValueChange = { viewModel.dispatchIntent(ExportFormIntent.SetIncludePageNumber(it)) },
            ),
        ) {
            TextCheckbox(
                title = getString(Res.string.page_number),
                checked = state.layout.includePageNumber,
                onCheckedChange = null,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        TextDivider(getString(Res.string.data))

        FormRow(
            icon = { ResourceIcon(Res.drawable.ic_note) },
            modifier = Modifier.toggleable(
                value = state.content.includeNotes,
                role = Role.Checkbox,
                onValueChange = { viewModel.dispatchIntent(ExportFormIntent.SetIncludeNotes(it)) },
            ),
        ) {
            TextCheckbox(
                title = getString(Res.string.notes),
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
                onValueChange = { viewModel.dispatchIntent(ExportFormIntent.SetIncludeTags(it)) },
            ),
        ) {
            TextCheckbox(
                title = getString(Res.string.tags),
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
                onValueChange = { viewModel.dispatchIntent(ExportFormIntent.SetIncludeDaysWithoutEntries(it)) },
            ),
        ) {
            TextCheckbox(
                title = getString(Res.string.days_without_entries),
                checked = state.layout.includeDaysWithoutEntries,
                onCheckedChange = null,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        TextDivider(getString(Res.string.measurement_categories))

        state.content.categories.forEach { category ->
            FormRow(
                icon = { MeasurementCategoryIcon(category.category) },
                modifier = Modifier.toggleable(
                    value = category.isExported,
                    role = Role.Checkbox,
                    onValueChange = {
                        val intent = ExportFormIntent.SetCategory(category.copy(isExported = !category.isExported))
                        viewModel.dispatchIntent(intent)
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