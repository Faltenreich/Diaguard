package com.faltenreich.diaguard.navigation.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.food.list.FoodList
import com.faltenreich.diaguard.food.list.FoodListIntent
import com.faltenreich.diaguard.food.list.FoodListMode
import com.faltenreich.diaguard.food.list.FoodListViewModel
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import com.faltenreich.diaguard.shared.view.SearchField
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.food
import diaguard.shared.generated.resources.food_new
import diaguard.shared.generated.resources.food_search_prompt
import diaguard.shared.generated.resources.ic_add
import org.jetbrains.compose.resources.painterResource
import org.koin.core.parameter.parametersOf

data class FoodListScreen(private val mode: FoodListMode) : Screen {

    override val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.food))
        }

    override val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<FoodListViewModel> { parametersOf(mode) }
                SearchField(
                    query = viewModel.query,
                    placeholder = getString(Res.string.food_search_prompt),
                    onQueryChange = { viewModel.query = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = AppTheme.dimensions.padding.P_3),
                )
            },
            floatingActionButton = {
                val viewModel = getViewModel<FoodListViewModel> { parametersOf(mode) }
                FloatingActionButton(onClick = { viewModel.dispatchIntent(FoodListIntent.Create) }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_add),
                        contentDescription = getString(Res.string.food_new),
                    )
                }
            }
        )

    @Composable
    override fun Content() {
        FoodList(viewModel = getViewModel { parametersOf(mode) })
    }
}