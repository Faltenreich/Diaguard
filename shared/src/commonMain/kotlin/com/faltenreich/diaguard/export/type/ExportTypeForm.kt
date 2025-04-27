package com.faltenreich.diaguard.export.type

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.export_type

@Composable
fun ExportTypeForm(
    selection: ExportType,
    onChange: (ExportType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = getString(Res.string.export_type),
            style = AppTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_3))

        Column(modifier = Modifier.selectableGroup()) {
            ExportType.entries.forEach { exportType ->
                ExportTypeListItem(
                    exportType = exportType,
                    isSelected = selection == exportType,
                    onClick = { onChange(exportType) },
                )
            }
        }

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_3))
    }
}