package com.faltenreich.diaguard.measurement.category.form

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.measurement.category.icon.MeasurementCategoryIcon
import com.faltenreich.diaguard.measurement.property.list.MeasurementPropertyList
import com.faltenreich.diaguard.shared.architecture.collectAsStateWithLifecycle
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.TextCheckbox
import com.faltenreich.diaguard.shared.view.TextInput
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.icon
import diaguard.shared.generated.resources.measurement_category_active
import diaguard.shared.generated.resources.measurement_category_active_disabled
import diaguard.shared.generated.resources.measurement_category_active_enabled
import diaguard.shared.generated.resources.name
import org.jetbrains.compose.resources.stringResource

@Composable
fun MeasurementCategoryForm(
    modifier: Modifier = Modifier,
    viewModel: MeasurementCategoryFormViewModel = inject(),
) {
    val state = viewModel.collectState()
    val name = viewModel.name.collectAsStateWithLifecycle().value
    val icon = viewModel.icon.collectAsStateWithLifecycle().value
    val isActive = viewModel.isActive.collectAsStateWithLifecycle().value

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        TextInput(
            input = name,
            onInputChange = { viewModel.name.value = it },
            label = getString(Res.string.name),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = AppTheme.dimensions.padding.P_1,
                    vertical = AppTheme.dimensions.padding.P_3,
                ),
        )

        Divider()

        Row(
            modifier = Modifier
                .clickable { viewModel.dispatchIntent(MeasurementCategoryFormIntent.OpenIconPicker) }
                .fillMaxWidth()
                .padding(all = AppTheme.dimensions.padding.P_3),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.icon),
                modifier = Modifier.weight(1f),
            )
            MeasurementCategoryIcon(
                icon = icon,
                fallback = name,
                modifier = Modifier.minimumInteractiveComponentSize(),
            )
        }

        Divider()

        TextCheckbox(
            title = stringResource(Res.string.measurement_category_active),
            subtitle = stringResource(
                if (isActive) Res.string.measurement_category_active_enabled
                else Res.string.measurement_category_active_disabled
            ),
            checked = viewModel.isActive.collectAsState().value,
            onCheckedChange = { viewModel.isActive.value = it },
        )

        AnimatedVisibility(
            visible = state != null,
            enter = fadeIn(),
        ) {
            val properties = state?.properties ?: emptyList()
            MeasurementPropertyList(
                category = viewModel.category,
                properties = properties,
            )
        }
    }
}