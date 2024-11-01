package com.faltenreich.diaguard.food.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.ClearButton
import com.faltenreich.diaguard.shared.view.SearchField
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.food_preferences_open
import diaguard.shared.generated.resources.food_search_prompt
import diaguard.shared.generated.resources.ic_arrow_back
import diaguard.shared.generated.resources.ic_preferences
import org.jetbrains.compose.resources.painterResource

@Composable
fun FoodSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    popScreen: () -> Unit,
    onOpenPreferences: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    SearchField(
        query = query,
        placeholder = getString(Res.string.food_search_prompt),
        onQueryChange = onQueryChange,
        leadingIcon = {
            IconButton(onClick = popScreen) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_back),
                    contentDescription = null,
                )
            }
        },
        trailingIcon = {
            Row {
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
                IconButton(onClick = onOpenPreferences) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_preferences),
                        contentDescription = getString(Res.string.food_preferences_open),
                    )
                }
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(all = AppTheme.dimensions.padding.P_2),
    )
}