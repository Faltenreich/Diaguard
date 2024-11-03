package com.faltenreich.diaguard.export.form

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
import com.faltenreich.diaguard.datetime.picker.DateRangePicker
import com.faltenreich.diaguard.measurement.category.icon.MeasurementCategoryIcon
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.DropdownButton
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
import diaguard.shared.generated.resources.merge_values
import diaguard.shared.generated.resources.notes
import diaguard.shared.generated.resources.page_number
import diaguard.shared.generated.resources.tags

@Composable
fun ExportForm(
    modifier: Modifier = Modifier,
    viewModel: ExportFormViewModel = inject(),
) {
    val state = viewModel.collectState() ?: return
    var showDateRangePicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        FormRow(icon = { ResourceIcon(Res.drawable.ic_time) }) {
            TextButton(onClick = { showDateRangePicker = true }) {
                Text(
                    text = state.date.dateRangeLocalized,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        Divider()
        FormRow(icon = { ResourceIcon(Res.drawable.ic_document) }) {
            DropdownButton(
                text = getString(state.type.selection.title),
                items = state.type.options.map { type ->
                    getString(type.title) to {
                        viewModel.dispatchIntent(ExportFormIntent.SelectType(type))
                    }
                }
            )
        }

        TextDivider(getString(Res.string.layout))
        FormRow(icon = { ResourceIcon(Res.drawable.ic_layout) }) {
            DropdownButton(
                text = getString(state.layout.selection.title),
                items = state.layout.options.map { layout ->
                    getString(layout.title) to {
                        viewModel.dispatchIntent(ExportFormIntent.SelectLayout(layout))
                    }
                },
            )
        }
        Divider()
        FormRow(icon = { ResourceIcon(Res.drawable.ic_position_top_left) }) {
            TextCheckbox(
                title = getString(Res.string.calendar_week),
                checked = state.date.includeCalendarWeek,
                onCheckedChange = { isChecked ->
                    viewModel.dispatchIntent(ExportFormIntent.SetIncludeCalendarWeek(isChecked))
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Divider()
        FormRow(icon = { ResourceIcon(Res.drawable.ic_position_bottom_left) }) {
            TextCheckbox(
                title = getString(Res.string.date_of_export),
                checked = state.date.includeDateOfExport,
                onCheckedChange = { isChecked ->
                    viewModel.dispatchIntent(ExportFormIntent.SetIncludeDateOfExport(isChecked))
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Divider()
        FormRow(icon = { ResourceIcon(Res.drawable.ic_position_bottom_right) }) {
            TextCheckbox(
                title = getString(Res.string.page_number),
                checked = state.layout.includePageNumber,
                onCheckedChange = { isChecked ->
                    viewModel.dispatchIntent(ExportFormIntent.SetIncludePageNumber(isChecked))
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        TextDivider(getString(Res.string.data))
        FormRow(icon = { ResourceIcon(Res.drawable.ic_note) }) {
            TextCheckbox(
                title = getString(Res.string.notes),
                checked = state.content.includeNotes,
                onCheckedChange = { isChecked ->
                    viewModel.dispatchIntent(ExportFormIntent.SetIncludeNotes(isChecked))
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Divider()
        FormRow(icon = { ResourceIcon(Res.drawable.ic_tag) }) {
            TextCheckbox(
                title = getString(Res.string.tags),
                checked = state.content.includeTags,
                onCheckedChange = { isChecked ->
                    viewModel.dispatchIntent(ExportFormIntent.SetIncludeTags(isChecked))
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Divider()
        FormRow(icon = { ResourceIcon(Res.drawable.ic_skip) }) {
            TextCheckbox(
                title = getString(Res.string.days_without_entries),
                checked = state.layout.includeDaysWithoutEntries,
                onCheckedChange = { isChecked ->
                    viewModel.dispatchIntent(ExportFormIntent.SetIncludeDaysWithoutEntries(isChecked))
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        TextDivider(getString(Res.string.measurement_categories))
        state.content.categories.forEach { category ->
            FormRow(icon = { MeasurementCategoryIcon(category.category) }) {
                TextCheckbox(
                    title = category.category.name,
                    checked = category.isExported,
                    onCheckedChange = { viewModel.setCategory(category.copy(isExported = !category.isExported)) },
                    modifier = Modifier.weight(1f),
                )
                if (true) { // TODO: category.category.properties.size > 1) {
                    TextCheckbox(
                        title = getString(Res.string.merge_values),
                        checked = category.isMerged,
                        onCheckedChange = { viewModel.setCategory(category.copy(isMerged = !category.isMerged)) },
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
            dateRange = state.date.dateRange,
            onPick = { dateRange ->
                showDateRangePicker = false
                viewModel.dispatchIntent(ExportFormIntent.SetDateRange(dateRange))
            }
        )
    }
}