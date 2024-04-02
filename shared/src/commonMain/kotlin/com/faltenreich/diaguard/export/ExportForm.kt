package com.faltenreich.diaguard.export

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyIcon
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.datetime.picker.DateRangePicker
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.DropdownButton
import com.faltenreich.diaguard.shared.view.DropdownTextMenuItem
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.ResourceIcon
import com.faltenreich.diaguard.shared.view.TextCheckbox
import com.faltenreich.diaguard.shared.view.TextDivider

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
                        onClick = { viewModel.exportType = type },
                        isSelected = { viewModel.exportType == type },
                    )
                }
            )
        }

        TextDivider(getString(MR.strings.layout))
        FormRow(icon = { ResourceIcon(MR.images.ic_layout) }) {
            DropdownButton(
                text = viewModel.pdfLayoutLocalized,
                items = viewModel.pdfLayouts.map { layout ->
                    DropdownTextMenuItem(
                        label = getString(layout.title),
                        onClick = { viewModel.pdfLayout = layout },
                        isSelected = { viewModel.pdfLayout == layout },
                    )
                },
            )
        }
        Divider()
        FormRow(icon = { ResourceIcon(MR.images.ic_position_top_left) }) {
            TextCheckbox(
                title = getString(MR.strings.calendar_week),
                checked = viewModel.includeCalendarWeek,
                onCheckedChange = { viewModel.includeCalendarWeek = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Divider()
        FormRow(icon = { ResourceIcon(MR.images.ic_position_bottom_left) }) {
            TextCheckbox(
                title = getString(MR.strings.date_of_export),
                checked = viewModel.includeDateOfExport,
                onCheckedChange = { viewModel.includeDateOfExport = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Divider()
        FormRow(icon = { ResourceIcon(MR.images.ic_position_bottom_right) }) {
            TextCheckbox(
                title = getString(MR.strings.page_number),
                checked = viewModel.includePageNumber,
                onCheckedChange = { viewModel.includePageNumber = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        TextDivider(getString(MR.strings.data))
        FormRow(icon = { ResourceIcon(MR.images.ic_note) }) {
            TextCheckbox(
                title = getString(MR.strings.notes),
                checked = viewModel.includeNotes,
                onCheckedChange = { viewModel.includeNotes = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Divider()
        FormRow(icon = { ResourceIcon(MR.images.ic_tag) }) {
            TextCheckbox(
                title = getString(MR.strings.tags),
                checked = viewModel.includeTags,
                onCheckedChange = { viewModel.includeTags = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Divider()
        FormRow(icon = { ResourceIcon(MR.images.ic_skip) }) {
            TextCheckbox(
                title = getString(MR.strings.days_without_entries),
                checked = viewModel.includeDaysWithoutEntries,
                onCheckedChange = { viewModel.includeDaysWithoutEntries = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        TextDivider(getString(MR.strings.measurement_properties))
        viewModel.properties.forEach { property ->
            FormRow(icon = { MeasurementPropertyIcon(property.property) }) {
                TextCheckbox(
                    title = property.property.name,
                    checked = property.isExported,
                    onCheckedChange = { viewModel.setProperty(property.copy(isExported = !property.isExported)) },
                    modifier = Modifier.weight(1f),
                )
                if (true) { // TODO: property.property.types.size > 1) {
                    TextCheckbox(
                        title = getString(MR.strings.merge_values),
                        checked = property.isMerged,
                        onCheckedChange = { viewModel.setProperty(property.copy(isMerged = !property.isMerged)) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
            Divider()
        }
    }

    AnimatedVisibility(
        visible = showDateRangePicker,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        DateRangePicker(
            dateRange = viewModel.dateRange,
            onPick = { dateRange ->
                showDateRangePicker = false
                viewModel.dateRange = dateRange
            }
        )
    }
}