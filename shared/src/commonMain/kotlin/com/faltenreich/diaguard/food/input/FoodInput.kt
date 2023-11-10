package com.faltenreich.diaguard.food.input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.navigation.screen.FoodListScreen
import com.faltenreich.diaguard.shared.localization.getString

@Composable
fun FoodInput(
    modifier: Modifier = Modifier,
    onAdd: (Food) -> Unit,
) {
    val navigator = LocalNavigator.currentOrThrow
    TextButton(
        onClick = { navigator.push(FoodListScreen(onSelection = onAdd)) },
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(getString(MR.strings.food_add))
    }
}