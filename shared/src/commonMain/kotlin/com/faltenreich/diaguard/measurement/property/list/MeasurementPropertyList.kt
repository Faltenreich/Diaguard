package com.faltenreich.diaguard.measurement.property.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormDialog
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.LoadingIndicator
import com.faltenreich.diaguard.shared.view.TextDivider
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.measurement_property_add
import diaguard.shared.generated.resources.measurement_properties
import org.jetbrains.compose.resources.stringResource

@Composable
fun MeasurementPropertyList(
    category: MeasurementCategory,
    properties: List<MeasurementProperty>,
    modifier: Modifier = Modifier,
    viewModel: MeasurementPropertyListViewModel = inject(),
) {
    Column(modifier = modifier) {
        TextDivider(getString(Res.string.measurement_properties))

        properties.forEachIndexed { index, property ->
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
                modifier = Modifier.clickable {
                    viewModel.dispatchIntent(
                        MeasurementPropertyListIntent.EditProperty(property)
                    )
                },
            )
            Divider()
        }

        FormRow {
            SuggestionChip(
                onClick = { viewModel.dispatchIntent(MeasurementPropertyListIntent.ShowFormDialog) },
                label = { Text(stringResource(Res.string.measurement_property_add)) },
            )
        }
    }

    when (val viewState = viewModel.collectState()) {
        null -> LoadingIndicator(modifier = modifier)
        else -> {
            // TODO: Replace with Modal
            if (viewState.showFormDialog) {
                MeasurementPropertyFormDialog(
                    onDismissRequest = {
                        viewModel.dispatchIntent(MeasurementPropertyListIntent.HideFormDialog)
                    },
                    onConfirmRequest = { propertyName, unitName ->
                        viewModel.dispatchIntent(
                            MeasurementPropertyListIntent.CreateProperty(
                                propertyName = propertyName,
                                unitName = unitName,
                                properties = properties,
                                categoryId = category.id,
                            )
                        )
                        viewModel.dispatchIntent(MeasurementPropertyListIntent.HideFormDialog)
                    }
                )
            }
        }
    }
}