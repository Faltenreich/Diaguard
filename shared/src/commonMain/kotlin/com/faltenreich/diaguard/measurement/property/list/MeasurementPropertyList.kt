package com.faltenreich.diaguard.measurement.property.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormDialog
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.ResourceIcon
import com.faltenreich.diaguard.shared.view.TextDivider
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_add
import diaguard.shared.generated.resources.measurement_properties
import diaguard.shared.generated.resources.measurement_property_add

@Composable
fun MeasurementPropertyList(
    category: MeasurementCategory.Local,
    properties: List<MeasurementProperty.Local>,
    modifier: Modifier = Modifier,
    viewModel: MeasurementPropertyListViewModel = inject(),
) {
    var showFormDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        TextDivider(getString(Res.string.measurement_properties))

        properties.forEachIndexed { index, property ->
            if (index != 0) {
                Divider()
            }
            MeasurementPropertyListItem(
                property = property,
                onArrowUp = {
                    viewModel.dispatchIntent(
                        MeasurementPropertyListIntent.DecrementSortIndex(property, properties)
                    )
                },
                showArrowUp = index > 0,
                onArrowDown = {
                    viewModel.dispatchIntent(
                        MeasurementPropertyListIntent.IncrementSortIndex(property, properties)
                    )
                },
                showArrowDown = index < properties.size - 1,
                modifier = Modifier
                    .clickable {
                        viewModel.dispatchIntent(
                            MeasurementPropertyListIntent.EditProperty(property)
                        )
                    }
                    .fillMaxWidth(),
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimensions.padding.P_1),
            contentAlignment = Alignment.Center,
        ) {
            SuggestionChip(
                onClick = { showFormDialog = true },
                label = { Text(getString(Res.string.measurement_property_add)) },
                icon = {
                    ResourceIcon(
                        icon = Res.drawable.ic_add,
                        modifier = modifier.size(InputChipDefaults.AvatarSize),
                    )
                },
            )
        }
    }

    if (showFormDialog) {
        MeasurementPropertyFormDialog(
            onDismissRequest = { showFormDialog = false },
            onConfirmRequest = { propertyName, unitName ->
                showFormDialog = false
                viewModel.dispatchIntent(
                    MeasurementPropertyListIntent.Store(
                        propertyName,
                        unitName,
                        category,
                        properties,
                    )
                )
            },
        )
    }
}