package com.faltenreich.diaguard.food.search

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.sharedViewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.food_new
import diaguard.shared.generated.resources.ic_add
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.koin.core.parameter.parametersOf

@Serializable
data class FoodSearchScreen(private val modeOrdinal: Int) : Screen {

    constructor(mode: FoodSearchMode) : this(modeOrdinal = mode.ordinal)

    @Composable
    override fun BottomAppBar(): BottomAppBarStyle {
        val viewModel = sharedViewModel<FoodSearchViewModel>(
            parameters = {
                parametersOf(FoodSearchMode.entries.first { it.ordinal == modeOrdinal })
            },
        )
        return BottomAppBarStyle.Visible(
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
        val viewModel = sharedViewModel<FoodSearchViewModel>(
            parameters = {
                parametersOf(FoodSearchMode.entries.first { it.ordinal == modeOrdinal })
            },
        )
        FoodSearch(viewModel = viewModel)
    }
}