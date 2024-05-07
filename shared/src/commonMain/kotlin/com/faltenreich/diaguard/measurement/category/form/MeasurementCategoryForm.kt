package com.faltenreich.diaguard.measurement.category.form

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.measurement.category.icon.MeasurementCategoryIcon
import com.faltenreich.diaguard.measurement.property.list.MeasurementPropertyList
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.TextInput
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.name

@Composable
fun MeasurementCategoryForm(
    modifier: Modifier = Modifier,
    viewModel: MeasurementCategoryFormViewModel = inject(),
) {
    val state = viewModel.collectState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        TextInput(
            input = viewModel.name.collectAsState().value,
            onInputChange = { input -> viewModel.name.value = input },
            label = getString(Res.string.name),
            leadingIcon = {
                IconButton(onClick = {
                    viewModel.dispatchIntent(MeasurementCategoryFormIntent.OpenIconPicker)
                }) {
                    MeasurementCategoryIcon(
                        icon = viewModel.icon.collectAsState().value,
                        fallback = viewModel.name.collectAsState().value,
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = AppTheme.dimensions.padding.P_1,
                    vertical = AppTheme.dimensions.padding.P_3,
                ),
        )

        AnimatedVisibility(
            visible = state != null,
            enter = fadeIn(),
        ) {
            val category = viewModel.category ?: return@AnimatedVisibility
            val properties = state?.properties ?: emptyList()
            MeasurementPropertyList(
                category = category,
                properties = properties,
            )
        }
    }
}