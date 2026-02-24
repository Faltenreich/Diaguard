package com.faltenreich.diaguard.export.form

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.measurement.category.icon.MeasurementCategoryIcon
import com.faltenreich.diaguard.view.checkbox.TextCheckbox
import com.faltenreich.diaguard.view.layout.FormRow
import com.faltenreich.diaguard.view.theme.AppTheme

@Composable
internal fun ExportFormCategoryListItem(
    category: ExportSettings.Category,
    onIntent: (ExportFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.animateContentSize()) {
        FormRow(
            icon = { MeasurementCategoryIcon(category.category) },
            modifier = Modifier
                .fillMaxWidth()
                .toggleable(
                    value = category.isExported,
                    role = Role.Checkbox,
                    onValueChange = {
                        val update = category.copy(isExported = it)
                        // TODO: onIntent(ExportFormIntent.SetSettings(update))
                    },
                ),
        ) {
            TextCheckbox(
                title = category.category.name,
                checked = category.isExported,
                onCheckedChange = null,
            )
        }

        if (category.isExported && category.properties.size > 1) {
            category.properties.forEach { property ->
                FormRow(
                    icon = { Spacer(modifier = Modifier.width(AppTheme.dimensions.size.ImageMedium)) },
                    modifier = Modifier.toggleable(
                        value = property.isExported,
                        role = Role.Checkbox,
                        onValueChange = { TODO() },
                    ),
                ) {
                    TextCheckbox(
                        title = property.property.name,
                        checked = property.isExported,
                        onCheckedChange = null,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}