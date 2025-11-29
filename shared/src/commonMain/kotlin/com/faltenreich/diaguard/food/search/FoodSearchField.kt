package com.faltenreich.diaguard.food.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.ClearButton
import com.faltenreich.diaguard.shared.view.SearchField
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.food_search_prompt
import diaguard.shared.generated.resources.ic_arrow_back
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FoodSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    popScreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    SearchField(
        query = query,
        placeholder = getString(Res.string.food_search_prompt),
        leadingIcon = {
            IconButton(onClick = popScreen) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_back),
                    contentDescription = null,
                )
            }
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = query.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                ClearButton(
                    onClick = {
                        onQueryChange("")
                        focusManager.clearFocus()
                    },
                )
            }
        },
        onQueryChange = onQueryChange,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun Preview() = AppPreview {
    FoodSearchField(
        query = "Query",
        onQueryChange = {},
        popScreen = {},
    )
}