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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.measurement.category.form.MeasurementCategoryFormIntent
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.ic_add
import com.faltenreich.diaguard.resource.measurement_properties
import com.faltenreich.diaguard.resource.measurement_property_add
import com.faltenreich.diaguard.view.divider.Divider
import com.faltenreich.diaguard.view.divider.TextDivider
import com.faltenreich.diaguard.view.image.ResourceIcon
import com.faltenreich.diaguard.view.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MeasurementPropertyList(
    properties: List<MeasurementProperty.Local>,
    onIntent: (MeasurementCategoryFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        TextDivider(stringResource(Res.string.measurement_properties))

        properties.forEachIndexed { index, property ->
            if (index != 0) {
                Divider()
            }
            MeasurementPropertyListItem(
                property = property,
                onArrowUp = {
                    onIntent(MeasurementCategoryFormIntent.DecrementSortIndex(property, properties))
                },
                showArrowUp = index > 0,
                onArrowDown = {
                    onIntent(MeasurementCategoryFormIntent.IncrementSortIndex(property, properties))
                },
                showArrowDown = index < properties.size - 1,
                modifier = Modifier
                    .clickable { onIntent(MeasurementCategoryFormIntent.EditProperty(property)) }
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
                onClick = { onIntent(MeasurementCategoryFormIntent.AddProperty) },
                label = { Text(stringResource(Res.string.measurement_property_add)) },
                icon = {
                    ResourceIcon(
                        icon = Res.drawable.ic_add,
                        modifier = Modifier.size(InputChipDefaults.AvatarSize),
                    )
                },
            )
        }
    }
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    MeasurementPropertyList(
        properties = listOf(property()),
        onIntent = {},
    )
}