package com.faltenreich.diaguard.preference.food

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.preference.list.PreferenceList
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.food_preferences_none_selected
import org.jetbrains.compose.resources.stringResource

@Composable
fun FoodPreferenceList(
    modifier: Modifier = Modifier,
    viewModel: FoodPreferenceListViewModel,
) {
    val state = viewModel.collectState() ?: return

    Column(modifier = modifier) {
        // TODO: Merge with other errors (and warnings), e.g. in EntryForm
        // FIXME: Improve animation to avoid jumping layout
        AnimatedVisibility(
            visible = !state.showAnyFood,
            enter = slideInVertically(),
            exit = slideOutVertically(),
        ) {
            Text(
                text = stringResource(Res.string.food_preferences_none_selected),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .fillMaxWidth()
                    .padding(
                        horizontal = AppTheme.dimensions.padding.P_3,
                        vertical = AppTheme.dimensions.padding.P_2,
                    ),
                color = MaterialTheme.colorScheme.onErrorContainer,
            )
        }
        PreferenceList(
            items = state.preferences,
            modifier = Modifier.weight(AppTheme.dimensions.weight.W_1),
        )
    }
}