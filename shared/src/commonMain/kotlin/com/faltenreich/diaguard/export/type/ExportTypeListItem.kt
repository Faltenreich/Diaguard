package com.faltenreich.diaguard.export.type

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.faltenreich.diaguard.AppTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun ExportTypeListItem(
    exportType: ExportType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .selectable(
                selected = isSelected,
                onClick = { onClick() },
                role = Role.RadioButton,
            )
            .padding(
                horizontal = AppTheme.dimensions.padding.P_3_5,
                vertical = AppTheme.dimensions.padding.P_3,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(stringResource(exportType.titleResource))
            Text(
                text = stringResource(exportType.descriptionResource),
                style = AppTheme.typography.bodySmall,
            )
        }
        RadioButton(
            selected = isSelected,
            onClick = null,
        )
    }
}