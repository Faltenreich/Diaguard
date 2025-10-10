package com.faltenreich.diaguard.food.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.faltenreich.diaguard.food.nutrient.FoodNutrientListItem
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.DeleteDialog
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.NoticeBar
import com.faltenreich.diaguard.shared.view.NoticeBarStyle
import com.faltenreich.diaguard.shared.view.ResourceIcon
import com.faltenreich.diaguard.shared.view.TextDivider
import com.faltenreich.diaguard.shared.view.TextInput
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.brand
import diaguard.shared.generated.resources.ic_brand
import diaguard.shared.generated.resources.ic_food
import diaguard.shared.generated.resources.ic_note
import diaguard.shared.generated.resources.ingredients
import diaguard.shared.generated.resources.name
import diaguard.shared.generated.resources.nutrients_per_100g
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FoodForm(
    state: FoodFormState?,
    onIntent: (FoodFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    state ?: return

    var name by remember { mutableStateOf(state.input.name) }
    var brand by remember { mutableStateOf(state.input.brand) }
    var ingredients by remember { mutableStateOf(state.input.ingredients) }

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
                            val update = state.input.copy(name = input)
                            onIntent(FoodFormIntent.SetInput(update))
                        },
                        placeholder = { Text(getString(Res.string.name)) },
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
                            val update = state.input.copy(brand = input)
                            onIntent(FoodFormIntent.SetInput(update))
                        },
                        placeholder = { Text(getString(Res.string.brand)) },
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
                            val update = state.input.copy(ingredients = input)
                            onIntent(FoodFormIntent.SetInput(update))
                        },
                        placeholder = { Text(getString(Res.string.ingredients)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                        ),
                    )
                }
            }

            TextDivider(getString(Res.string.nutrients_per_100g))

            state.input.nutrients.forEachIndexed { index, data ->
                if (index != 0) {
                    Divider()
                }
                FoodNutrientListItem(
                    data = data,
                    onUpdate = { update ->
                        onIntent(FoodFormIntent.SetNutrient(update))
                    },
                )
            }
        }

        NoticeBar(
            text = state.error ?: "",
            isVisible = state.error != null,
            style = NoticeBarStyle.ERROR,
        )
    }

    if (state.deleteDialog != null) {
        DeleteDialog(
            onDismissRequest = {
                onIntent(FoodFormIntent.CloseDeleteDialog)
            },
            onConfirmRequest = {
                onIntent(FoodFormIntent.CloseDeleteDialog)
                onIntent(FoodFormIntent.Delete(needsConfirmation = false))
            },
        )
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    val food = food()
    FoodForm(
        state = FoodFormState(
            food = food,
            input = with (food) {
                FoodFormInput(
                    name = name,
                    brand = brand ?: "",
                    ingredients = ingredients ?: "",
                    labels = labels ?: "",
                    carbohydrates = carbohydrates.toString(),
                    energy = energy?.toString() ?: "",
                    fat = fat?.toString() ?: "",
                    fatSaturated = fatSaturated?.toString() ?: "",
                    fiber = fiber?.toString() ?: "",
                    proteins = proteins?.toString() ?: "",
                    salt = salt?.toString() ?: "",
                    sodium = sodium?.toString() ?: "",
                    sugar = sugar?.toString() ?: "",
                )
            },
            error = null,
            deleteDialog = null,
        ),
        onIntent = {},
    )
}