package com.faltenreich.diaguard.food.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.food.nutrient.FoodNutrientList
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.ResourceIcon
import com.faltenreich.diaguard.shared.view.TextInput

@Composable
fun FoodForm(
    modifier: Modifier = Modifier,
    viewModel: FoodFormViewModel = inject(),
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        FormRow(icon = { ResourceIcon(MR.images.ic_food) }) {
            TextInput(
                input = viewModel.name,
                onInputChange = { viewModel.name = it },
                label = getString(MR.strings.name),
            )
        }
        Divider()
        FormRow(icon = { ResourceIcon(MR.images.ic_brand) }) {
            TextInput(
                input = viewModel.brand,
                onInputChange = { viewModel.brand = it },
                label = getString(MR.strings.brand),
            )
        }
        Divider()
        FormRow(icon = { ResourceIcon(MR.images.ic_note) }) {
            TextInput(
                input = viewModel.ingredients,
                onInputChange = { viewModel.ingredients = it },
                label = getString(MR.strings.ingredients),
            )
        }
        FoodNutrientList(
            data = viewModel.nutrientData,
            onIntent = viewModel::handleIntent,
        )
    }
}