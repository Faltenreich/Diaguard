package com.faltenreich.diaguard.food.eaten.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.data.food.eaten.FoodEaten
import com.faltenreich.diaguard.view.divider.Divider
import com.faltenreich.diaguard.data.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.no_entries
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FoodEatenList(
    state: FoodEatenListState?,
    onIntent: (FoodEatenListIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        null -> Unit
        is FoodEatenListState.Empty -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(stringResource(Res.string.no_entries))
        }
        is FoodEatenListState.NonEmpty -> Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            state.results.forEach { foodEaten ->
                FoodEatenListItem(
                    foodEaten = foodEaten,
                    onIntent = onIntent,
                )
                Divider()
            }
        }
    }
}

@Preview
@Composable
private fun EmptyPreview() = AppPreview {
    FoodEatenList(
        state = FoodEatenListState.Empty,
        onIntent = {},
    )
}

@Preview
@Composable
private fun NonEmptyPreview() = AppPreview {
    FoodEatenList(
        state = FoodEatenListState.NonEmpty(
            results = listOf(
                FoodEaten.Localized(
                    local = foodEaten(),
                    dateTime = now().toString(),
                    amountInGrams = "20",
                )
            ),
        ),
        onIntent = {},
    )
}