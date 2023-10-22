package com.faltenreich.diaguard.export

import androidx.compose.foundation.layout.Box
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
import com.faltenreich.diaguard.shared.view.DropdownTextMenu
import com.faltenreich.diaguard.shared.view.DropdownTextMenuItem
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.ResourceIcon

@Composable
fun ExportForm(
    modifier: Modifier = Modifier,
    viewModel: ExportFormViewModel = inject(),
) {
    var showDateRangePicker by remember { mutableStateOf(false) }
    var isDocumentTypeDropDownExpanded by remember { mutableStateOf(false) }
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
            Box {
                TextButton(
                    onClick = { isDocumentTypeDropDownExpanded = true },
                ) {
                    Text(
                        text = viewModel.exportTypeLocalized,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                DropdownTextMenu(
                    expanded = isDocumentTypeDropDownExpanded,
                    onDismissRequest = { isDocumentTypeDropDownExpanded = false },
                    items = listOf(
                        DropdownTextMenuItem(
                            label = getString(MR.strings.pdf),
                            onClick = { viewModel.exportType.value = ExportType.PDF },
                            isSelected = { viewModel.exportType.value == ExportType.PDF },
                        ),
                        DropdownTextMenuItem(
                            label = getString(MR.strings.csv),
                            onClick = { viewModel.exportType.value = ExportType.CSV },
                            isSelected = { viewModel.exportType.value == ExportType.CSV },
                        ),
                    ),
                )
            }
        }
        Divider()
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