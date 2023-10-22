package com.faltenreich.diaguard.export

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.DateRangePicker
import com.faltenreich.diaguard.shared.view.DropdownButton
import com.faltenreich.diaguard.shared.view.DropdownTextMenuItem
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.FormRowLabel
import com.faltenreich.diaguard.shared.view.ResourceIcon
import com.faltenreich.diaguard.shared.view.TextCheckbox

@Composable
fun ExportForm(
    modifier: Modifier = Modifier,
    viewModel: ExportFormViewModel = inject(),
) {
    var showDateRangePicker by remember { mutableStateOf(false) }
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        FormRow(icon = { ResourceIcon(MR.images.ic_time) }) {
            TextButton(onClick = { showDateRangePicker = true }) {
                Text(
                    text = viewModel.dateRangeLocalized,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        Divider()
        FormRow(icon = { ResourceIcon(MR.images.ic_document) }) {
            DropdownButton(
                text = viewModel.exportTypeLocalized,
                items = viewModel.exportTypes.map { type ->
                    DropdownTextMenuItem(
                        label = getString(type.title),
                        onClick = { viewModel.exportType.value = type },
                        isSelected = { viewModel.exportType.value == type },
                    )
                }
            )
        }

        FormRowLabel(getString(MR.strings.layout))
        FormRow(icon = { ResourceIcon(MR.images.ic_layout) }) {
            DropdownButton(
                text = viewModel.pdfLayoutLocalized,
                items = viewModel.pdfLayouts.map { layout ->
                    DropdownTextMenuItem(
                        label = getString(layout.title),
                        onClick = { viewModel.pdfLayout.value = layout },
                        isSelected = { viewModel.pdfLayout.value == layout },
                    )
                },
            )
        }
        Divider()
        FormRow(icon = { ResourceIcon(MR.images.ic_position_top_left) }) {
            TextCheckbox(
                text = getString(MR.strings.calendar_week),
                checked = viewModel.includeCalendarWeek.value,
                onCheckedChange = { viewModel.includeCalendarWeek.value = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Divider()
        FormRow(icon = { ResourceIcon(MR.images.ic_position_bottom_left) }) {
            TextCheckbox(
                text = getString(MR.strings.date_of_export),
                checked = viewModel.includeDateOfExport.value,
                onCheckedChange = { viewModel.includeDateOfExport.value = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Divider()
        FormRow(icon = { ResourceIcon(MR.images.ic_position_bottom_right) }) {
            TextCheckbox(
                text = getString(MR.strings.page_number),
                checked = viewModel.includePageNumber.value,
                onCheckedChange = { viewModel.includePageNumber.value = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        FormRowLabel(getString(MR.strings.data))
        FormRow(icon = { ResourceIcon(MR.images.ic_note) }) {
            TextCheckbox(
                text = getString(MR.strings.notes),
                checked = viewModel.includeNotes.value,
                onCheckedChange = { viewModel.includeNotes.value = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Divider()
        FormRow(icon = { ResourceIcon(MR.images.ic_tag) }) {
            TextCheckbox(
                text = getString(MR.strings.tags),
                checked = viewModel.includeTags.value,
                onCheckedChange = { viewModel.includeTags.value = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Divider()
        FormRow(icon = { ResourceIcon(MR.images.ic_skip) }) {
            TextCheckbox(
                text = getString(MR.strings.days_without_entries),
                checked = viewModel.includeDaysWithoutEntries.value,
                onCheckedChange = { viewModel.includeDaysWithoutEntries.value = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        FormRowLabel(getString(MR.strings.measurement_properties))
    }

    if (showDateRangePicker) {
        DateRangePicker(
            dateRange = viewModel.dateRange.value,
            onPick = { dateRange ->
                showDateRangePicker = false
                viewModel.dateRange.value = dateRange
            }
        )
    }
}