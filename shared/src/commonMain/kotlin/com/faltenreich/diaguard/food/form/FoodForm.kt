package com.faltenreich.diaguard.food.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.faltenreich.diaguard.food.nutrient.FoodNutrientList
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.NoticeBar
import com.faltenreich.diaguard.shared.view.NoticeBarStyle
import com.faltenreich.diaguard.shared.view.ResourceIcon
import com.faltenreich.diaguard.shared.view.TextInput
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.brand
import diaguard.shared.generated.resources.ic_brand
import diaguard.shared.generated.resources.ic_food
import diaguard.shared.generated.resources.ic_note
import diaguard.shared.generated.resources.ingredients
import diaguard.shared.generated.resources.name

@Composable
fun FoodForm(
    viewModel: FoodFormViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.collectState() ?: return

    var name by remember { mutableStateOf(state.name) }
    var brand by remember { mutableStateOf(state.brand) }
    var ingredients by remember { mutableStateOf(state.ingredients) }

    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f),
        ) {
            Card(shape = RectangleShape) {
                FormRow(icon = { ResourceIcon(Res.drawable.ic_food) }) {
                    TextInput(
                        input = name,
                        onInputChange = { input ->
                            name = input
                            viewModel.dispatchIntent(FoodFormIntent.SetName(input))
                        },
                        label = getString(Res.string.name),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                        ),
                    )
                }

                Divider()

                FormRow(icon = { ResourceIcon(Res.drawable.ic_brand) }) {
                    TextInput(
                        input = brand,
                        onInputChange = { input ->
                            brand = input
                            viewModel.dispatchIntent(FoodFormIntent.SetBrand(input))
                        },
                        label = getString(Res.string.brand),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                        ),
                    )
                }

                Divider()

                FormRow(icon = { ResourceIcon(Res.drawable.ic_note) }) {
                    TextInput(
                        input = ingredients,
                        onInputChange = { input ->
                            ingredients = input
                            viewModel.dispatchIntent(FoodFormIntent.SetIngredients(input))
                        },
                        label = getString(Res.string.ingredients),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                        ),
                    )
                }
            }

            FoodNutrientList(
                data = viewModel.nutrientData,
                onIntent = viewModel::dispatchIntent,
            )
        }

        NoticeBar(
            text = state.error ?: "",
            isVisible = state.error != null,
            style = NoticeBarStyle.ERROR,
        )
    }
}