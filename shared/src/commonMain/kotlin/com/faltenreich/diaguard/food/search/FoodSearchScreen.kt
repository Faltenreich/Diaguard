package com.faltenreich.diaguard.food.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.localization.di.sharedViewModel
import com.faltenreich.diaguard.localization.di.viewModel
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.food_new
import diaguard.shared.generated.resources.food_preferences_open
import diaguard.shared.generated.resources.ic_add
import diaguard.shared.generated.resources.ic_preferences
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf

@Serializable
data class FoodSearchScreen(private val modeOrdinal: Int) : Screen {

    constructor(mode: FoodSearchMode) : this(modeOrdinal = mode.ordinal)

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        val viewModel = viewModel<FoodSearchViewModel>(
            parameters = {
                parametersOf(FoodSearchMode.entries.first { it.ordinal == modeOrdinal })
            },
        )
        var query by remember { mutableStateOf("") }
        return TopAppBarStyle.Custom {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppTheme.colors.scheme.primary)
                    .padding(WindowInsets.statusBars.asPaddingValues())
                    .padding(horizontal = AppTheme.dimensions.padding.P_2)
                    // Prevent jumping layout
                    .defaultMinSize(minHeight = TopAppBarDefaults.MediumAppBarCollapsedHeight),
                contentAlignment = Alignment.Center,
            ) {
                FoodSearchField(
                    query = query,
                    onQueryChange = { input ->
                        query = input
                        viewModel.dispatchIntent(FoodSearchIntent.SetQuery(input))
                    },
                    popScreen = { viewModel.dispatchIntent(FoodSearchIntent.Close) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }

    @Composable
    override fun BottomAppBar(): BottomAppBarStyle {
        val viewModel = viewModel<FoodSearchViewModel>(
            parameters = {
                parametersOf(FoodSearchMode.entries.first { it.ordinal == modeOrdinal })
            },
        )
        return BottomAppBarStyle.Visible(
            actions = {
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_preferences),
                    contentDescription = stringResource(Res.string.food_preferences_open),
                    onClick = { viewModel.dispatchIntent(FoodSearchIntent.OpenPreferences) },
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    painter = painterResource(Res.drawable.ic_add),
                    contentDescription = stringResource(Res.string.food_new),
                    onClick = { viewModel.dispatchIntent(FoodSearchIntent.Create) },
                )
            }
        )
    }

    @Composable
    override fun Content() {
        val viewModel = viewModel<FoodSearchViewModel>(
            parameters = {
                parametersOf(FoodSearchMode.entries.first { it.ordinal == modeOrdinal })
            },
        )
        val selectionViewModel = sharedViewModel<FoodSelectionViewModel>()

        FoodSearch(
            state = viewModel.collectState(),
            onSelect = { food ->
                when (viewModel.mode) {
                    FoodSearchMode.STROLL -> viewModel.dispatchIntent(FoodSearchIntent.OpenFood(food))
                    FoodSearchMode.FIND -> {
                        selectionViewModel.postEvent(FoodSelectionEvent.Select(food))
                        viewModel.dispatchIntent(FoodSearchIntent.Close)
                    }
                }
            },
        )
    }
}