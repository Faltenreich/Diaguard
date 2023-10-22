package com.faltenreich.diaguard.export

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.DateRangePicker
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
                DropdownMenu(
                    expanded = isDocumentTypeDropDownExpanded,
                    onDismissRequest = { isDocumentTypeDropDownExpanded = false },
                    modifier = modifier,
                ) {
                    Text(
                        text = getString(MR.strings.pdf),
                        modifier = Modifier
                            .clickable {
                                viewModel.exportType.value = ExportType.PDF
                                isDocumentTypeDropDownExpanded = false
                            }
                            .fillMaxWidth()
                            .background(
                                if (viewModel.exportType.value == ExportType.PDF) AppTheme.colors.material.secondaryContainer
                                else Color.Transparent
                            )
                            .padding(all = AppTheme.dimensions.padding.P_3),
                    )
                    Text(
                        text = getString(MR.strings.csv),
                        modifier = Modifier
                            .clickable {
                                viewModel.exportType.value = ExportType.CSV
                                isDocumentTypeDropDownExpanded = false
                            }
                            .fillMaxWidth()
                            .background(
                                if (viewModel.exportType.value == ExportType.CSV) AppTheme.colors.material.secondaryContainer
                                else Color.Transparent
                            )
                            .padding(all = AppTheme.dimensions.padding.P_3),
                    )
                }
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