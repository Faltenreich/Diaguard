package com.faltenreich.diaguard.food.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.view.button.ClearButton
import com.faltenreich.diaguard.view.input.SearchField
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.food_search_prompt
import diaguard.shared.generated.resources.ic_arrow_back
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FoodSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onBackIconClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    SearchField(
        query = query,
        placeholder = stringResource(Res.string.food_search_prompt),
        leadingIcon = {
            IconButton(onClick = onBackIconClick) {
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
private fun Preview() = PreviewScaffold {
    FoodSearchField(
        query = "Query",
        onQueryChange = {},
        onBackIconClick = {},
    )
}