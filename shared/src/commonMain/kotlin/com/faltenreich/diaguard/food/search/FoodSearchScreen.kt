package com.faltenreich.diaguard.food.search

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.viewModel
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

    override val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.Hidden

    override val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible(
            floatingActionButton = {
                // TODO: Convert to shared ViewModel by passing parent as owner
                val viewModel = viewModel<FoodSearchViewModel>(
                    viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current),
                    parameters = {
                        val mode = FoodSearchMode.entries.first { it.ordinal == modeOrdinal }
                        parametersOf(mode)
                    },
                )
                FloatingActionButton(onClick = { viewModel.dispatchIntent(FoodSearchIntent.Create) }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_add),
                        contentDescription = getString(Res.string.food_new),
                    )
                }
            }
        )

    @Composable
    override fun Content() {
        FoodSearch(
            viewModel = viewModel(
                // TODO: Convert to shared ViewModel by passing parent as owner
                viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current),
                parameters = {
                    val mode = FoodSearchMode.entries.first { it.ordinal == modeOrdinal }
                    parametersOf(mode)
                },
            )
        )
    }
}