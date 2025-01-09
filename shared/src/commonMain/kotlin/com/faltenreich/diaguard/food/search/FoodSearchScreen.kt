package com.faltenreich.diaguard.food.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.sharedViewModel
import com.faltenreich.diaguard.shared.di.viewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.food_new
import diaguard.shared.generated.resources.food_preferences_open
import diaguard.shared.generated.resources.ic_add
import diaguard.shared.generated.resources.ic_preferences
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
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
        return TopAppBarStyle.Custom {
            Column(
                modifier = Modifier
                    .background(AppTheme.colors.scheme.primary)
                    .padding(WindowInsets.statusBars.asPaddingValues()),
            ) {
                FoodSearchField(
                    query = viewModel.query,
                    onQueryChange = { viewModel.query = it },
                    popScreen = { viewModel.dispatchIntent(FoodSearchIntent.Close) },
                )
                FoodSearchHeader(
                    modifier = Modifier
                        .padding(
                            start = AppTheme.dimensions.padding.P_3,
                            end = AppTheme.dimensions.padding.P_3,
                            bottom = AppTheme.dimensions.padding.P_2,
                        ),
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
                    contentDescription = Res.string.food_preferences_open,
                    onClick = { viewModel.dispatchIntent(FoodSearchIntent.OpenPreferences) },
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { viewModel.dispatchIntent(FoodSearchIntent.Create) }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_add),
                        contentDescription = getString(Res.string.food_new),
                    )
                }
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
        FoodSearch(
            viewModel = viewModel,
            selectionViewModel = sharedViewModel(),
        )
    }
}