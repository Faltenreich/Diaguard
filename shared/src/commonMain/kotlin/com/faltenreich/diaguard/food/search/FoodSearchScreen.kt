package com.faltenreich.diaguard.food.search

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
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

    override val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.Hidden

    override val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible(
            floatingActionButton = {
                FloatingActionButton(onClick = { TODO() }) { // viewModel.dispatchIntent(FoodSearchIntent.Create) }) {
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
            viewModel = sharedViewModel(
                parameters = {
                    parametersOf(FoodSearchMode.entries.first { it.ordinal == modeOrdinal })
                },
            )
        )
    }
}